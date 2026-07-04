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

import com.agendamento.barbearia.feature.agendamento.dto.AgendamentoRequestDTO;
import com.agendamento.barbearia.feature.agendamento.dto.AgendamentoResponseDTO;
import com.agendamento.barbearia.feature.agendamento.mapper.AgendamentoMapper;
import com.agendamento.barbearia.feature.agendamento.model.Agendamento;
import com.agendamento.barbearia.feature.agendamento.repo.AgendamentoRepository;
import com.agendamento.barbearia.feature.horario.model.HorarioTrabalho;
import com.agendamento.barbearia.feature.horario.repo.HorarioTrabalhoRepository;
import com.agendamento.barbearia.feature.profissional.repo.ProfissionalRepository;
import com.agendamento.barbearia.feature.profissional_servico.model.ProfissionalServico;
import com.agendamento.barbearia.feature.profissional_servico.repo.ProfissionalServicoRepository;
import com.agendamento.barbearia.feature.servico.model.Servico;
import com.agendamento.barbearia.feature.servico.repo.ServicoRepository;
import com.agendamento.barbearia.feature.usuario.repo.UsuarioRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

/**
* Camada de serviço responsável pelas regras de negócio de agendamentos,
* incluindo o cálculo de disponibilidade e validações contra choques de horários.
*/
@Service
@RequiredArgsConstructor
public class AgendamentoService {
  
  private final AgendamentoRepository repo;
  private final UsuarioRepository usuarioRepo;
  private final ProfissionalRepository profissionalRepo;
  private final ServicoRepository servicoRepo;
  private final ProfissionalServicoRepository profissionalServicoRepo; 
  private final HorarioTrabalhoRepository horarioRepo;
  private final AgendamentoMapper mapper;
  
  public List<AgendamentoResponseDTO> findAllComServicos() {
    return repo.findAllComServicos()
    .stream()
    .map(mapper::toResponseDTO)
    .collect(Collectors.toList());
  }
  
  public List<AgendamentoResponseDTO> buscarAgendaDoCliente(Long clienteId) {
    return repo.findByClienteIdComServicos(clienteId)
    .stream()
    .map(mapper::toResponseDTO)
    .toList();
  }
  
  public List<AgendamentoResponseDTO> buscarAgendaDoBarbeiro(Long profissionalId) {
    return repo.findByProfissionalIdComServicos(profissionalId)
    .stream()
    .map(mapper::toResponseDTO)
    .toList();
  }
  
  /**
  * Calcula dinamicamente os blocos de horários livres de um profissional na data selecionada,
  * cruzando sua escala de trabalho semanal com a duração dos agendamentos já existentes.
  */
  public List<String> buscarHorariosDisponiveis(Long profissionalId, LocalDate data) {
    DayOfWeek diaSemana = data.getDayOfWeek();
    
    List<HorarioTrabalho> turnos = horarioRepo.findByProfissionalIdAndDiaSemana(profissionalId, diaSemana);
    if (turnos.isEmpty()) {
      return Collections.emptyList(); 
    }
    
    LocalDateTime inicioDoDia = data.atStartOfDay();
    LocalDateTime fimDoDia = data.atTime(23, 59, 59);
    List<Agendamento> agendamentosDoDia = repo.buscarAgendaDoBarbeiroNoDia(profissionalId, inicioDoDia, fimDoDia);
    
    List<String> horariosLivres = new ArrayList<>();
    int intervaloMinutos = 30; 
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
    
    for (HorarioTrabalho turno : turnos) {
      LocalTime horaSlot = turno.getHoraInicio();
      
      while (!horaSlot.plusMinutes(intervaloMinutos).isAfter(turno.getHoraFim())) {
        LocalDateTime inicioSlot = data.atTime(horaSlot);
        LocalDateTime fimSlot = inicioSlot.plusMinutes(intervaloMinutos);
        
        // Verifica sobreposição considerando o início e a soma das durações dos serviços do agendamento
        boolean temColisao = agendamentosDoDia.stream().anyMatch(agendamento -> {
          LocalDateTime aInicio = agendamento.getDataHora();
          int duracao = agendamento.getServicos().stream().mapToInt(Servico::getDuracaoMinutos).sum();
          LocalDateTime aFim = aInicio.plusMinutes(duracao);
          
          return inicioSlot.isBefore(aFim) && fimSlot.isAfter(aInicio);
        });
        
        if (!temColisao) {
          horariosLivres.add(horaSlot.format(formatter));
        }
        horaSlot = horaSlot.plusMinutes(intervaloMinutos);
      }
    }
    return horariosLivres;
  }
  
  /**
  * Registra um novo agendamento validando retroatividade, se o profissional realiza os serviços,
  * se está dentro do horário de expediente e se não há colisão com reservas anteriores.
  */
  @Transactional
  public AgendamentoResponseDTO create(AgendamentoRequestDTO requestDTO) {
    
    if (requestDTO.getDataHora().isBefore(LocalDateTime.now())) {
      throw new IllegalArgumentException("Não é possível agendar para uma data no passado.");
    }
    
    List<ProfissionalServico> vinculos = profissionalServicoRepo.findByProfissionalId(requestDTO.getProfissionalId());
    List<Long> idsServicosPermitidos = vinculos.stream()
    .map(vinculo -> vinculo.getServico().getId())
    .toList();
    
    boolean profissionalFazOsServicos = requestDTO.getServicoIds().stream()
    .allMatch(idsServicosPermitidos::contains);
    
    if (!profissionalFazOsServicos) {
      throw new IllegalArgumentException("Erro: O profissional selecionado não realiza um ou mais serviços solicitados.");
    }
    
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
    
    boolean dentroDoExpediente = turnosDoDia.stream().anyMatch(turno -> 
      !horaInicioDesejada.isBefore(turno.getHoraInicio()) && 
      !horaFimDesejada.isAfter(turno.getHoraFim())           
    );
    
    if (!dentroDoExpediente) {
      throw new IllegalArgumentException("Fora do horário de expediente! O barbeiro não atende neste horário.");
    }
    
    LocalDateTime inicioDoDia = inicioDesejado.toLocalDate().atStartOfDay();
    LocalDateTime fimDoDia = inicioDesejado.toLocalDate().atTime(23, 59, 59);
    
    List<Agendamento> agendaDoDia = repo.buscarAgendaDoBarbeiroNoDia(
      requestDTO.getProfissionalId(), inicioDoDia, fimDoDia
    );
    
    // Validação estrita de conflito de horários (Anti-Double Booking)
    for (Agendamento agendamentoExistente : agendaDoDia) {
      LocalDateTime inicioExistente = agendamentoExistente.getDataHora();
      
      int duracaoExistente = agendamentoExistente.getServicos().stream()
      .mapToInt(Servico::getDuracaoMinutos).sum();
      LocalDateTime fimExistente = inicioExistente.plusMinutes(duracaoExistente);
      
      if (inicioDesejado.isBefore(fimExistente) && fimDesejado.isAfter(inicioExistente)) {
        throw new RuntimeException("Horário indisponível! O barbeiro já tem atendimento reservado neste intervalo.");
      }
    }
    
    Agendamento agendamento = new Agendamento();
    agendamento.setDataHora(inicioDesejado);
    agendamento.setStatus("CONFIRMADO"); 
    
    agendamento.setCliente(usuarioRepo.findById(requestDTO.getClienteId())
    .orElseThrow(() -> new RuntimeException("Cliente não encontrado")));
    
    agendamento.setProfissional(profissionalRepo.findById(requestDTO.getProfissionalId())
    .orElseThrow(() -> new RuntimeException("Profissional não encontrado")));
    
    agendamento.setServicos(servicosEscolhidos);
    
    Agendamento agendamentoSalvo = repo.save(agendamento);
    return mapper.toResponseDTO(agendamentoSalvo);
  }
  
  @Transactional
  public void cancelar(Long id) {
    Agendamento agendamento = repo.findById(id)
    .orElseThrow(() -> new RuntimeException("Agendamento não encontrado"));
    
    agendamento.setStatus("CANCELADO");
    repo.save(agendamento);
  }
}