package com.agendamento.barbearia.feature.agendamento.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import com.agendamento.barbearia.feature.agendamento.model.Agendamento;
import com.agendamento.barbearia.feature.agendamento.repo.AgendamentoRepository;
import com.agendamento.barbearia.feature.agendamento.dto.AgendamentoRequestDTO;
import com.agendamento.barbearia.feature.agendamento.dto.AgendamentoResponseDTO;
import com.agendamento.barbearia.feature.agendamento.mapper.AgendamentoMapper;
import com.agendamento.barbearia.feature.usuario.repo.UsuarioRepository;
import com.agendamento.barbearia.feature.profissional.repo.ProfissionalRepository;
import com.agendamento.barbearia.feature.servico.repo.ServicoRepository;

@Service
@RequiredArgsConstructor
public class AgendamentoService {
  
  private final AgendamentoRepository repo;
  private final UsuarioRepository usuarioRepo;
  private final ProfissionalRepository profissionalRepo;
  private final ServicoRepository servicoRepo;
  private final AgendamentoMapper mapper;
  
  public List<AgendamentoResponseDTO> findAllComServicos() {
    return repo.findAllComServicos()
    .stream()
    .map(mapper::toResponseDTO)
    .collect(Collectors.toList());
  }
  
  @Transactional
  public AgendamentoResponseDTO create(AgendamentoRequestDTO requestDTO) {
    
    if (requestDTO.getDataHora().isBefore(LocalDateTime.now())) {
      throw new IllegalArgumentException("Não é possível agendar em uma data no passado.");
    }
    
    Agendamento agendamento = new Agendamento();
    agendamento.setDataHora(requestDTO.getDataHora());
    agendamento.setStatus("PENDENTE");
    
    // Buscar as entidades relacionadas no banco a partir dos IDs enviados no DTO
    agendamento.setCliente(usuarioRepo.findById(requestDTO.getClienteId())
    .orElseThrow(() -> new RuntimeException("Cliente não encontrado")));
    
    agendamento.setProfissional(profissionalRepo.findById(requestDTO.getProfissionalId())
    .orElseThrow(() -> new RuntimeException("Profissional não encontrado")));
    
    agendamento.setServicos(servicoRepo.findAllById(requestDTO.getServicoIds()));
    
    Agendamento agendamentoSalvo = repo.save(agendamento);
    
    // Retorna os dados seguros em formato DTO
    return mapper.toResponseDTO(agendamentoSalvo);
  }
}