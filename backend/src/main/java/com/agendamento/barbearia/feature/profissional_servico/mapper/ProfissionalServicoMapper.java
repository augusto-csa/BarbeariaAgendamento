package com.agendamento.barbearia.feature.profissional_servico.mapper;

import org.springframework.stereotype.Component;

import com.agendamento.barbearia.feature.profissional_servico.dto.ProfissionalServicoResponseDTO;
import com.agendamento.barbearia.feature.profissional_servico.model.ProfissionalServico;
import com.agendamento.barbearia.feature.servico.mapper.ServicoMapper;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProfissionalServicoMapper {
  
  private final ServicoMapper servicoMapper;
  
  public ProfissionalServicoResponseDTO toResponseDTO(ProfissionalServico pServico){
    if(pServico == null) return null;
    
    ProfissionalServicoResponseDTO dto = new ProfissionalServicoResponseDTO();
    
    dto.setId(pServico.getId());
    dto.setProfissionalId(pServico.getProfissional().getId());
    dto.setServico(servicoMapper.toResponseDTO(pServico.getServico()));
    
    return dto;
  }
}
