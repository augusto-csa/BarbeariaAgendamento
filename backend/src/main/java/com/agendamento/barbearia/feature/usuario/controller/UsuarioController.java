package com.agendamento.barbearia.feature.usuario.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.agendamento.barbearia.feature.usuario.model.Usuario;
import com.agendamento.barbearia.feature.usuario.repo.UsuarioRepository;
import com.agendamento.barbearia.feature.usuario.service.UsuarioService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService service;

    @GetMapping
    public List<Usuario> listarTodos() {
        return service.findAll();
    }
}