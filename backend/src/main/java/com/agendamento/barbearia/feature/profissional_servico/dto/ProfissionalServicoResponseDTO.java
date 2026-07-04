package com.agendamento.barbearia.feature.profissional_servico.dto;
import com.agendamento.barbearia.feature.servico.dto.ServicoResponseDTO;
import lombok.Data;

@Data
public class ProfissionalServicoResponseDTO {
  private Long id;
  private Long profissionalId;
  private ServicoResponseDTO servico; 
}