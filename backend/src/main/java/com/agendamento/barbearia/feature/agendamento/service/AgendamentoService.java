package com.agendamento.barbearia.feature.agendamento.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.agendamento.barbearia.feature.agendamento.model.Agendamento;
import com.agendamento.barbearia.feature.agendamento.repo.AgendamentoRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AgendamentoService {
  
  private final AgendamentoRepository repo;
  
  public List<Agendamento> findAllComServicos() {
    return repo.findAllComServicos();
  }
  
  @Transactional
  public Agendamento criar(Agendamento agendamento) {
    
    // Aqui entra a Regra de Negócio (O trabalho do Chef de Cozinha)
    if (agendamento.getDataHora().isBefore(LocalDateTime.now())) {
      throw new IllegalArgumentException("Não é possível agendar em uma data no passado.");
    }
    
    agendamento.setStatus("PENDENTE");
    
    return repo.save(agendamento);
  }
}
