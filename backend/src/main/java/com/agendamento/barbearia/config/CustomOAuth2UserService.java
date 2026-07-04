package com.agendamento.barbearia.config;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.agendamento.barbearia.feature.usuario.model.Usuario;
import com.agendamento.barbearia.feature.usuario.repo.UsuarioRepository;

import lombok.RequiredArgsConstructor;

/**
* Intercepta o fluxo de autenticação OAuth2 (ex: Google) para gerenciar o usuário na base de dados local.
*/
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
  
  private final UsuarioRepository usuarioRepo;
  
  @Override
  public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
    OAuth2User oAuth2User = super.loadUser(userRequest);
    
    String email = oAuth2User.getAttribute("email");
    String nome = oAuth2User.getAttribute("name");
    
    // Registra automaticamente o usuário caso seja o seu primeiro acesso via login social
    usuarioRepo.findByEmail(email).orElseGet(() -> {
      Usuario novo = new Usuario();
      novo.setNome(nome);
      novo.setEmail(email);
      novo.setRole("CLIENTE");
      novo.setSenha(""); 
      return usuarioRepo.save(novo);
    });
    
    return oAuth2User;
  }
}