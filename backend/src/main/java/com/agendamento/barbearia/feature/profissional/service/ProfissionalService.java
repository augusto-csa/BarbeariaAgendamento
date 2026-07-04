package com.agendamento.barbearia.feature.profissional.service;

import com.agendamento.barbearia.feature.profissional.dto.ProfissionalRequestDTO;
import com.agendamento.barbearia.feature.profissional.dto.ProfissionalResponseDTO;
import com.agendamento.barbearia.feature.profissional.mapper.ProfissionalMapper;
import com.agendamento.barbearia.feature.profissional.model.Profissional;
import com.agendamento.barbearia.feature.profissional.repo.ProfissionalRepository;
import com.agendamento.barbearia.feature.usuario.model.Usuario;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* Camada de serviço responsável pelas regras de negócio e gerenciamento dos perfis dos profissionais (barbeiros).
*/
@Service
@RequiredArgsConstructor
public class ProfissionalService {
  
  private final ProfissionalRepository repository;
  private final ProfissionalMapper mapper;
  
  /**
  * Retorna a lista de todos os profissionais cadastrados, mapeando as entidades para DTOs.
  */
  public List<ProfissionalResponseDTO> listarTodos() {
    return repository.findlAllComAgendamentos()
    .stream()
    .map(mapper::toResponseDTO)
    .toList();
  }
  
  public ProfissionalResponseDTO buscarPorId(Long id) {
    Profissional profissional = repository.findById(id)
    .orElseThrow(() -> new RuntimeException("Profissional não encontrado"));
    
    return mapper.toResponseDTO(profissional);
  }
  
  /**
  * Vincula as informações específicas de trabalho (perfil profissional) a uma conta de usuário recém-criada.
  */
  public ProfissionalResponseDTO criarPerfilProfissional(ProfissionalRequestDTO request, Usuario usuario) {
    Profissional p = new Profissional();
    p.setBiografia(request.getBiografia());
    p.setUsuario(usuario);
    
    Profissional salvo = repository.save(p);
    return mapper.toResponseDTO(salvo);
  }
}