package com.agendamento.barbearia.config;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.agendamento.barbearia.feature.usuario.model.Usuario;
import com.agendamento.barbearia.feature.usuario.repo.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UsuarioRepository usuarioRepo;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        
        String email = oAuth2User.getAttribute("email");
        String nome = oAuth2User.getAttribute("name");
        
        // Se o usuário não existir no nosso banco, nós criamos ele agora!
        usuarioRepo.findByEmail(email).orElseGet(() -> {
            Usuario novo = new Usuario();
            novo.setNome(nome);
            novo.setEmail(email);
            novo.setRole("CLIENTE");
            novo.setSenha(""); // Login social não usa senha local
            return usuarioRepo.save(novo);
        });

        return oAuth2User;
    }
}