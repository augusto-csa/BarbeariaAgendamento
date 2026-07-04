package com.agendamento.barbearia.feature.profissional.facade;

import org.springframework.stereotype.Component;

import com.agendamento.barbearia.feature.profissional.dto.ProfissionalRequestDTO;
import com.agendamento.barbearia.feature.profissional.dto.ProfissionalResponseDTO;
import com.agendamento.barbearia.feature.profissional.service.ProfissionalService;
import com.agendamento.barbearia.feature.usuario.model.Usuario;
import com.agendamento.barbearia.feature.usuario.service.UsuarioService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

/**
* Facade responsável por orquestrar o registro de um novo barbeiro, 
* unificando a criação das credenciais de acesso e do perfil profissional.
*/
@Component
@RequiredArgsConstructor
public class RegistroProfissionalFacade {
  
  private final UsuarioService usuarioService;
  private final ProfissionalService profissionalService;
  
  /**
  * Executa o cadastro completo de forma atômica (transacional). 
  * Se houver falha na criação do perfil profissional, a criação do usuário é revertida (rollback).
  */
  @Transactional 
  public ProfissionalResponseDTO registrarNovoBarbeiro(ProfissionalRequestDTO request) {
    Usuario novoUsuario = usuarioService.criarContaBarbeiro(request);
    return profissionalService.criarPerfilProfissional(request, novoUsuario);
  }
}