package com.agendamento.barbearia.feature.usuario.dto;

import lombok.Data;

@Data
public class UsuarioResponseDTO {
    private Long id;
    private String nome;
    private String email;
    private String telefone;
    private String role;
}
