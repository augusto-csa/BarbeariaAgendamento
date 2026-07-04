package com.agendamento.barbearia.feature.profissional_servico.dto;
import lombok.Data;

@Data
public class ProfissionalServicoRequestDTO {
  private Long profissionalId;
  private Long servicoId;
}