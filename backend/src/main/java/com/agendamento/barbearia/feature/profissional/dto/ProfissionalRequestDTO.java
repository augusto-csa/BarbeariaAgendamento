package com.agendamento.barbearia.feature.profissional.dto;

import lombok.Data;

@Data
public class ProfissionalRequestDTO {
    private String nome;
    private String email;
    private String telefone;
    private String senha;
    private String fotoUrl;
    private String biografia;
}