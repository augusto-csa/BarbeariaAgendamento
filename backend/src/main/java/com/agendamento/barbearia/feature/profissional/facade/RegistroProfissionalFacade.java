package com.agendamento.barbearia.feature.profissional.facade;

import org.springframework.stereotype.Component;

import com.agendamento.barbearia.feature.profissional.dto.ProfissionalRequestDTO;
import com.agendamento.barbearia.feature.profissional.dto.ProfissionalResponseDTO;
import com.agendamento.barbearia.feature.profissional.service.ProfissionalService;
import com.agendamento.barbearia.feature.usuario.model.Usuario;
import com.agendamento.barbearia.feature.usuario.service.UsuarioService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RegistroProfissionalFacade {

    private final UsuarioService usuarioService;
    private final ProfissionalService profissionalService;

    // A anotação mais importante: Ou salva TUDO, ou não salva NADA.
    @Transactional 
    public ProfissionalResponseDTO registrarNovoBarbeiro(ProfissionalRequestDTO request) {
        
        // 1. Pede ao UsuarioService para criar a conta de acesso
        Usuario novoUsuario = usuarioService.criarContaBarbeiro(request);

        // 2. Passa o Usuário já salvo (com ID) para o ProfissionalService fazer a parte dele
        return profissionalService.criarPerfilProfissional(request, novoUsuario);
    }
}