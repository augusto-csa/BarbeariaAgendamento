package com.agendamento.barbearia.feature.usuario.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import com.agendamento.barbearia.feature.profissional.model.Profissional;
import com.agendamento.barbearia.feature.profissional.repo.ProfissionalRepository;
import com.agendamento.barbearia.feature.usuario.dto.UsuarioRequestDTO;
import com.agendamento.barbearia.feature.usuario.dto.UsuarioResponseDTO;
import com.agendamento.barbearia.feature.usuario.model.Usuario;
import com.agendamento.barbearia.feature.usuario.repo.UsuarioRepository;
import com.agendamento.barbearia.feature.usuario.service.UsuarioService;

import lombok.RequiredArgsConstructor;

/**
* Endpoint para gestão de contas de usuário e recuperação dos dados da sessão ativa.
*/
@RequiredArgsConstructor
@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
  
  private final UsuarioService service;
  private final UsuarioRepository usuarioRepository;
  private final ProfissionalRepository profissionalRepository;
  
  /**
  * Retorna os dados do usuário atualmente autenticado (via OAuth2 ou autenticação local).
  * Caso o usuário possua a role "BARBEIRO", o ID do seu perfil profissional é anexado à resposta.
  */
  @GetMapping("/me")
  public ResponseEntity<?> getLoggedUser(Authentication authentication) {
    if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("logged", false));
    }
    
    String email = null;
    
    if (authentication.getPrincipal() instanceof OAuth2User oauth2User) {
      email = oauth2User.getAttribute("email");
    } else if (authentication.getPrincipal() instanceof UserDetails userDetails) {
      email = userDetails.getUsername();
    } else {
      email = authentication.getName();
    }
    
    Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);
    if (usuarioOpt.isEmpty()) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("logged", false));
    }
    
    Usuario user = usuarioOpt.get();
    Map<String, Object> response = new HashMap<>();
    response.put("id", user.getId());
    response.put("nome", user.getNome());
    response.put("email", user.getEmail());
    response.put("fotoUrl", user.getFotoUrl());
    response.put("role", user.getRole());
    
    if ("BARBEIRO".equals(user.getRole())) {
      Optional<Profissional> profOpt = profissionalRepository.findByUsuarioId(user.getId());
      profOpt.ifPresent(profissional -> response.put("profissionalId", profissional.getId()));
    }
    
    return ResponseEntity.ok(response);
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