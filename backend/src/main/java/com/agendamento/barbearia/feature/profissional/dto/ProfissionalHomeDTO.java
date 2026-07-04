package com.agendamento.barbearia.feature.profissional.dto;

import lombok.Data;

@Data
public class ProfissionalHomeDTO {
  private Long id; 
  private String nome;
  private String fotoUrl;
  private Double notaMedia;
  private Integer totalAvaliacoes;
}