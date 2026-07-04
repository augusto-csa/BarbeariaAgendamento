package com.agendamento.barbearia.feature.servico.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ServicoResponseDTO {
  private Long id;
  private String nome;
  private String descricao;
  private BigDecimal preco;
  private Integer duracaoMinutos;
}