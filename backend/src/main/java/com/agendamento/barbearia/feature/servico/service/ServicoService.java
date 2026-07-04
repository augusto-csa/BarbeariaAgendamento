package com.agendamento.barbearia.feature.servico.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.agendamento.barbearia.feature.servico.dto.ServicoRequestDTO;
import com.agendamento.barbearia.feature.servico.dto.ServicoResponseDTO;
import com.agendamento.barbearia.feature.servico.mapper.ServicoMapper;
import com.agendamento.barbearia.feature.servico.model.Servico;
import com.agendamento.barbearia.feature.servico.repo.ServicoRepository;

import lombok.RequiredArgsConstructor;

/**
* Camada de serviço responsável por gerenciar o catálogo global de serviços da barbearia,
* servindo como intermediária entre o controlador e o acesso a dados.
*/
@RequiredArgsConstructor
@Service
public class ServicoService {
  
  private final ServicoMapper mapper;
  private final ServicoRepository repo;
  
  public List<ServicoResponseDTO> findAll() {
    return repo.findAll()
    .stream()
    .map(mapper::toResponseDTO)
    .toList();
  }
  
  public ServicoResponseDTO save(ServicoRequestDTO servico) {
    Servico s = mapper.toEntity(servico);
    return mapper.toResponseDTO(repo.save(s));
  }
}