package com.agendamento.barbearia.feature.usuario.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.agendamento.barbearia.feature.usuario.model.Usuario;
import com.agendamento.barbearia.feature.usuario.repo.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UsuarioService {
    
    private final UsuarioRepository repo;

    public List<Usuario> findAll() {
        return repo.findAll();
    }


}
