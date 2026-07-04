package com.agendamento.barbearia.feature.profissional.mapper;

import com.agendamento.barbearia.feature.avaliacao.model.Avaliacao;
import com.agendamento.barbearia.feature.profissional.dto.ProfissionalRequestDTO;
import com.agendamento.barbearia.feature.profissional.dto.ProfissionalResponseDTO;
import com.agendamento.barbearia.feature.profissional.model.Profissional;
import com.agendamento.barbearia.feature.usuario.model.Usuario;

import org.springframework.stereotype.Component;

/**
* Classe responsável por mapear os dados entre as entidades JPA e os objetos de transferência (DTOs),
* isolando a lógica de exibição externa do modelo de dados interno.
*/
@Component
public class ProfissionalMapper {
  
  /**
  * Converte a entidade Profissional em um DTO de resposta, consolidando os dados
  * do usuário vinculado e calculando dinamicamente a média aritmética das avaliações.
  */
  public ProfissionalResponseDTO toResponseDTO(Profissional profissional) {
    if (profissional == null) return null;
    
    ProfissionalResponseDTO dto = new ProfissionalResponseDTO();
    dto.setId(profissional.getId());
    dto.setBiografia(profissional.getBiografia());
    
    if (profissional.getUsuario() != null) {
      dto.setNome(profissional.getUsuario().getNome());
      dto.setFotoUrl(profissional.getUsuario().getFotoUrl());
    }
    
    int totalAvaliacoes = profissional.getAvaliacoes() != null ? profissional.getAvaliacoes().size() : 0;
    dto.setTotalAvaliacoes(totalAvaliacoes);
    
    if (totalAvaliacoes > 0) {
      double soma = profissional.getAvaliacoes().stream()
      .mapToDouble(Avaliacao::getNota)
      .sum();
      // Arredonda a média para uma casa decimal (ex: 4.75 -> 4.8)
      dto.setNotaMedia(Math.round((soma / totalAvaliacoes) * 10.0) / 10.0);
    } else {
      dto.setNotaMedia(0.0);
    }
    
    return dto;
  }
  
  /**
  * Converte um DTO de requisição para a entidade Profissional, inicializando
  * em conjunto a entidade Usuario com o papel estrito de BARBEIRO.
  */
  public Profissional toEntity(ProfissionalRequestDTO dto) {
    if (dto == null) return null;
    
    Profissional profissional = new Profissional();
    profissional.setBiografia(dto.getBiografia());
    
    Usuario usuario = new Usuario();
    usuario.setNome(dto.getNome());
    usuario.setEmail(dto.getEmail());
    usuario.setTelefone(dto.getTelefone());
    usuario.setSenha(dto.getSenha()); 
    usuario.setFotoUrl(dto.getFotoUrl());
    usuario.setRole("BARBEIRO"); 
    
    profissional.setUsuario(usuario);
    
    return profissional;
  }
}