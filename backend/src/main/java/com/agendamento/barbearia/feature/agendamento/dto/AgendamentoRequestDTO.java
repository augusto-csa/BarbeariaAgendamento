package com.agendamento.barbearia.feature.agendamento.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
public class AgendamentoRequestDTO {
  private Long clienteId;
  private Long profissionalId;
  private LocalDateTime dataHora;
  private List<Long> servicoIds;
}