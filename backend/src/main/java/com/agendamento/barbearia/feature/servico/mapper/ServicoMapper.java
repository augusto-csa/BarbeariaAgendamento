package com.agendamento.barbearia.feature.servico.mapper;

import com.agendamento.barbearia.feature.servico.dto.ServicoRequestDTO;
import com.agendamento.barbearia.feature.servico.dto.ServicoResponseDTO;
import com.agendamento.barbearia.feature.servico.model.Servico;
import org.springframework.stereotype.Component;

@Component
public class ServicoMapper {
  
  public Servico toEntity(ServicoRequestDTO dto) {
    if (dto == null) return null;
    Servico servico = new Servico();
    servico.setNome(dto.getNome());
    servico.setDescricao(dto.getDescricao());
    servico.setPreco(dto.getPreco());
    servico.setDuracaoMinutos(dto.getDuracaoMinutos());
    return servico;
  }
  
  public ServicoResponseDTO toResponseDTO(Servico servico) {
    if (servico == null) return null;
    ServicoResponseDTO dto = new ServicoResponseDTO();
    dto.setId(servico.getId());
    dto.setNome(servico.getNome());
    dto.setDescricao(servico.getDescricao());
    dto.setPreco(servico.getPreco());
    dto.setDuracaoMinutos(servico.getDuracaoMinutos());
    return dto;
  }
}