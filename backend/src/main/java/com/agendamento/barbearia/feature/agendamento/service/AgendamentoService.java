package com.agendamento.barbearia.feature.agendamento.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import com.agendamento.barbearia.feature.agendamento.model.Agendamento;
import com.agendamento.barbearia.feature.agendamento.repo.AgendamentoRepository;
import com.agendamento.barbearia.feature.horario.model.HorarioTrabalho;
import com.agendamento.barbearia.feature.horario.repo.HorarioTrabalhoRepository;
import com.agendamento.barbearia.feature.agendamento.dto.AgendamentoRequestDTO;
import com.agendamento.barbearia.feature.agendamento.dto.AgendamentoResponseDTO;
import com.agendamento.barbearia.feature.agendamento.mapper.AgendamentoMapper;
import com.agendamento.barbearia.feature.usuario.repo.UsuarioRepository;
import com.agendamento.barbearia.feature.profissional.repo.ProfissionalRepository;
import com.agendamento.barbearia.feature.profissional_servico.model.ProfissionalServico;
import com.agendamento.barbearia.feature.profissional_servico.repo.ProfissionalServicoRepository;
import com.agendamento.barbearia.feature.servico.model.Servico;
import com.agendamento.barbearia.feature.servico.repo.ServicoRepository;

@Service
@RequiredArgsConstructor
public class AgendamentoService {
  
  private final AgendamentoRepository repo;
  private final UsuarioRepository usuarioRepo;
  private final ProfissionalRepository profissionalRepo;
  private final ServicoRepository servicoRepo;
  private final AgendamentoMapper mapper;
  private final ProfissionalServicoRepository profissionalServicoRepo;
  private final HorarioTrabalhoRepository horarioRepo;
  
  public List<AgendamentoResponseDTO> findAllComServicos() {
    return repo.findAllComServicos()
    .stream()
    .map(mapper::toResponseDTO)
    .collect(Collectors.toList());
  }
  
  @Transactional
  public AgendamentoResponseDTO create(AgendamentoRequestDTO requestDTO) {
    
    // 1. Validação básica de passado
    if (requestDTO.getDataHora().isBefore(LocalDateTime.now())) {
      throw new IllegalArgumentException("Não é possível agendar para uma data no passado.");
    }
    
    List<ProfissionalServico> vinculos = profissionalServicoRepo.findByProfissionalId(requestDTO.getProfissionalId());
    
    // Extrai apenas os IDs dos serviços que o profissional sabe fazer
    List<Long> idsServicosPermitidos = vinculos.stream()
            .map(vinculo -> vinculo.getServico().getId())
            .toList();

    // Verifica se TODOS os serviços solicitados estão na lista de permitidos
    boolean profissionalFazOsServicos = requestDTO.getServicoIds().stream()
            .allMatch(idsServicosPermitidos::contains);

    if (!profissionalFazOsServicos) {
        throw new IllegalArgumentException("Erro: O profissional selecionado não realiza um ou mais serviços solicitados.");
    }
    
    // 2. Busca os serviços escolhidos para calcular a duração total desejada
    List<Servico> servicosEscolhidos = servicoRepo.findAllById(requestDTO.getServicoIds());
    if (servicosEscolhidos.isEmpty()) {
        throw new IllegalArgumentException("É necessário escolher pelo menos um serviço.");
    }
    
    int duracaoTotalMinutos = servicosEscolhidos.stream().mapToInt(Servico::getDuracaoMinutos).sum();
    LocalDateTime inicioDesejado = requestDTO.getDataHora();
    LocalDateTime fimDesejado = inicioDesejado.plusMinutes(duracaoTotalMinutos);

    DayOfWeek diaDesejado = inicioDesejado.getDayOfWeek();
    LocalTime horaInicioDesejada = inicioDesejado.toLocalTime();
    LocalTime horaFimDesejada = fimDesejado.toLocalTime();

    List<HorarioTrabalho> turnosDoDia = horarioRepo.findByProfissionalIdAndDiaSemana(
            requestDTO.getProfissionalId(), diaDesejado
    );

    // Verifica se o agendamento cabe INTEIRAMENTE dentro de pelo menos um dos turnos
    boolean dentroDoExpediente = turnosDoDia.stream().anyMatch(turno -> 
        !horaInicioDesejada.isBefore(turno.getHoraInicio()) && // Não começa antes do turno
        !horaFimDesejada.isAfter(turno.getHoraFim())           // Não termina depois do turno
    );

    if (!dentroDoExpediente) {
        throw new IllegalArgumentException("Fora do horário de expediente! O barbeiro não atende neste horário na " + diaDesejado);
    }

    // 3. Busca a agenda do barbeiro APENAS para o dia solicitado (Performance!)
    LocalDateTime inicioDoDia = inicioDesejado.toLocalDate().atStartOfDay();
    LocalDateTime fimDoDia = inicioDesejado.toLocalDate().atTime(23, 59, 59);
    
    List<Agendamento> agendaDoDia = repo.buscarAgendaDoBarbeiroNoDia(
        requestDTO.getProfissionalId(), inicioDoDia, fimDoDia
    );

    // 4. REGRA DE NEGÓCIO: Validação de Colisão (O Coração do Sistema)
    for (Agendamento agendamentoExistente : agendaDoDia) {
        LocalDateTime inicioExistente = agendamentoExistente.getDataHora();
        
        // Calcula quando termina o agendamento que já está no banco
        int duracaoExistente = agendamentoExistente.getServicos().stream()
                .mapToInt(Servico::getDuracaoMinutos).sum();
        LocalDateTime fimExistente = inicioExistente.plusMinutes(duracaoExistente);

        // A fórmula matemática da interceção de tempos!
        if (inicioDesejado.isBefore(fimExistente) && fimDesejado.isAfter(inicioExistente)) {
            throw new RuntimeException("Horário indisponível! O barbeiro já tem atendimento reservado neste intervalo.");
        }
    }

    // 5. Se sobreviveu às validações, podemos salvar em segurança!
    Agendamento agendamento = new Agendamento();
    agendamento.setDataHora(inicioDesejado);
    agendamento.setStatus("CONFIRMADO"); // Pode já salvar como confirmado
    
    agendamento.setCliente(usuarioRepo.findById(requestDTO.getClienteId())
        .orElseThrow(() -> new RuntimeException("Cliente não encontrado")));
    
    agendamento.setProfissional(profissionalRepo.findById(requestDTO.getProfissionalId())
        .orElseThrow(() -> new RuntimeException("Profissional não encontrado")));
    
    agendamento.setServicos(servicosEscolhidos);
    
    Agendamento agendamentoSalvo = repo.save(agendamento);
    return mapper.toResponseDTO(agendamentoSalvo);
  }

  public List<String> buscarHorariosDisponiveis(Long profissionalId, LocalDate data) {
        DayOfWeek diaSemana = data.getDayOfWeek();
        
        // 1. Busca os turnos de trabalho do barbeiro naquele dia da semana
        List<HorarioTrabalho> turnos = horarioRepo.findByProfissionalIdAndDiaSemana(profissionalId, diaSemana);
        if (turnos.isEmpty()) {
            return Collections.emptyList(); // Não trabalha neste dia
        }

        // 2. Busca o que já está marcado neste dia
        LocalDateTime inicioDoDia = data.atStartOfDay();
        LocalDateTime fimDoDia = data.atTime(23, 59, 59);
        List<Agendamento> agendamentosDoDia = repo.buscarAgendaDoBarbeiroNoDia(profissionalId, inicioDoDia, fimDoDia);

        List<String> horariosLivres = new ArrayList<>();
        int intervaloMinutos = 30; // Vamos gerar horários de 30 em 30 minutos

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        // 3. Para cada turno (ex: Manhã, Tarde), gera os "slots"
        for (HorarioTrabalho turno : turnos) {
            LocalTime horaSlot = turno.getHoraInicio();
            
            // Enquanto o slot couber dentro do turno
            while (!horaSlot.plusMinutes(intervaloMinutos).isAfter(turno.getHoraFim())) {
                LocalDateTime inicioSlot = data.atTime(horaSlot);
                LocalDateTime fimSlot = inicioSlot.plusMinutes(intervaloMinutos);

                // Verifica se este slot choca com algum agendamento existente
                boolean temColisao = agendamentosDoDia.stream().anyMatch(agendamento -> {
                    LocalDateTime aInicio = agendamento.getDataHora();
                    int duracao = agendamento.getServicos().stream().mapToInt(Servico::getDuracaoMinutos).sum();
                    LocalDateTime aFim = aInicio.plusMinutes(duracao);
                    
                    // Fórmula de intersecção
                    return inicioSlot.isBefore(aFim) && fimSlot.isAfter(aInicio);
                });

                if (!temColisao) {
                    horariosLivres.add(horaSlot.format(formatter));
                }
                
                // Avança para o próximo bloco de 30 mins
                horaSlot = horaSlot.plusMinutes(intervaloMinutos);
            }
        }

        return horariosLivres;
    }

    public List<AgendamentoResponseDTO> buscarAgendaDoBarbeiro(Long profissionalId) {
      return repo.findByProfissionalIdComServicos(profissionalId)
              .stream()
              .map(mapper::toResponseDTO)
              .toList();
  }
}