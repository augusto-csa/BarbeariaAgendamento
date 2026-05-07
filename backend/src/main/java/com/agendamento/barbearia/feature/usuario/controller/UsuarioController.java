package com.agendamento.barbearia.feature.usuario.controller;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.agendamento.barbearia.feature.usuario.dto.UsuarioRequestDTO;
import com.agendamento.barbearia.feature.usuario.dto.UsuarioResponseDTO;
import com.agendamento.barbearia.feature.usuario.service.UsuarioService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
    
    private final UsuarioService service;
    
    @GetMapping("/me")
    public Map<String, Object> getLoggedUser(@AuthenticationPrincipal OAuth2User principal) {
        if (principal == null) {
            return Collections.singletonMap("logged", false);
        }
        // Retorna os dados que o Google enviou (nome, email, foto)
        return principal.getAttributes(); 
    }
    
    @GetMapping
    public List<UsuarioResponseDTO> listarTodos() {
        return service.findAll();
    }
    
    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> create(@RequestBody UsuarioRequestDTO request) {
        return ResponseEntity.ok(service.create(request));
    }
}