package com.agendamento.barbearia.feature.profissional.mapper;

import com.agendamento.barbearia.feature.avaliacao.model.Avaliacao;
import com.agendamento.barbearia.feature.profissional.dto.ProfissionalRequestDTO;
import com.agendamento.barbearia.feature.profissional.dto.ProfissionalResponseDTO;
import com.agendamento.barbearia.feature.profissional.model.Profissional;
import com.agendamento.barbearia.feature.usuario.model.Usuario;

import org.springframework.stereotype.Component;

@Component
public class ProfissionalMapper {

    // Transforma Entidade (Banco) -> Response DTO (React)
    public ProfissionalResponseDTO toResponseDTO(Profissional profissional) {
        if (profissional == null) return null;

        ProfissionalResponseDTO dto = new ProfissionalResponseDTO();
        dto.setId(profissional.getId());
        dto.setBiografia(profissional.getBiografia());
        
        // Dados do Usuário associado
        if (profissional.getUsuario() != null) {
            dto.setNome(profissional.getUsuario().getNome());
            dto.setFotoUrl(profissional.getUsuario().getFotoUrl());
        }
        
        // Lógica de cálculo da nota
        int totalAvaliacoes = profissional.getAvaliacoes() != null ? profissional.getAvaliacoes().size() : 0;
        dto.setTotalAvaliacoes(totalAvaliacoes);
        
        if (totalAvaliacoes > 0) {
            double soma = profissional.getAvaliacoes().stream()
                                      .mapToDouble(Avaliacao::getNota)
                                      .sum();
            dto.setNotaMedia(Math.round((soma / totalAvaliacoes) * 10.0) / 10.0);
        } else {
            dto.setNotaMedia(0.0);
        }
        
        return dto;
    }

    public Profissional toEntity(ProfissionalRequestDTO dto) {
        if (dto == null) return null;

        Profissional profissional = new Profissional();
        profissional.setBiografia(dto.getBiografia());

        // Como o Profissional depende de um Usuario, nós criamos e preenchemos ele aqui
        Usuario usuario = new Usuario();
        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        usuario.setTelefone(dto.getTelefone());
        usuario.setSenha(dto.getSenha()); // A senha ainda está em texto puro aqui!
        usuario.setFotoUrl(dto.getFotoUrl());
        usuario.setRole("BARBEIRO"); // Força o papel correto no sistema

        // Amarra o usuário ao profissional
        profissional.setUsuario(usuario);

        return profissional;
    }
    
}