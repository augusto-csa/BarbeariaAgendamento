package com.agendamento.barbearia.feature.usuario.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UsuarioRequestDTO {    
    @Schema(example = "Joao")
    private String nome;
    @Schema(example = "joao@gmail.com")
    private String email;
    @Schema(example = "12123456789")
    private String telefone;
    @Schema(example = "senha")
    private String senha;
    @Schema(example = "CLIENTE")
    private String role;
}
