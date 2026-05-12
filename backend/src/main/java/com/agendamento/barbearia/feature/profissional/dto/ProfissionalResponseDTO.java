package com.agendamento.barbearia.feature.profissional.dto;

import lombok.Data;

@Data
public class ProfissionalResponseDTO {
    private Long id;
    private String nome;
    private String fotoUrl;
    private String biografia;
    private Double notaMedia; 
    private Integer totalAvaliacoes;
}