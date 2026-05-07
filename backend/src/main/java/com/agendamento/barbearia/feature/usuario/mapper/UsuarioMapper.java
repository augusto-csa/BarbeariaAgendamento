package com.agendamento.barbearia.feature.usuario.mapper;

import org.springframework.stereotype.Component;

import com.agendamento.barbearia.feature.usuario.dto.UsuarioRequestDTO;
import com.agendamento.barbearia.feature.usuario.dto.UsuarioResponseDTO;
import com.agendamento.barbearia.feature.usuario.model.Usuario;

@Component
public class UsuarioMapper {
    
    public UsuarioResponseDTO toResponse(Usuario usuario){
        UsuarioResponseDTO dto = new UsuarioResponseDTO();
        dto.setId(usuario.getId());
        dto.setNome(usuario.getNome());
        dto.setEmail(usuario.getEmail());
        dto.setTelefone(usuario.getTelefone());
        dto.setRole(usuario.getRole());
        return dto;
    }

    public Usuario toEntity(UsuarioRequestDTO usuario){
        Usuario user = new Usuario();
        user.setNome(usuario.getNome());
        user.setEmail(usuario.getEmail());
        user.setSenha(usuario.getSenha());
        user.setTelefone(usuario.getTelefone());
        user.setRole(usuario.getRole());
        return user;
    }
}
