package com.agendamento.barbearia.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import lombok.RequiredArgsConstructor;

/**
* Configuração central de segurança, gerenciando permissões de rotas, autenticação OAuth2 e políticas de CORS.
*/
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
  
  private final CustomOAuth2UserService customOAuth2UserService;
  
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
  
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
    .cors(cors -> cors.configurationSource(corsConfigurationSource())) 
    .csrf(csrf -> csrf.disable())
    .authorizeHttpRequests(auth -> auth
      .requestMatchers("/auth/**", "/public/**", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
      .requestMatchers(HttpMethod.POST, "/usuarios").permitAll()
      .requestMatchers(HttpMethod.GET, "/profissionais").permitAll()
      .requestMatchers(HttpMethod.POST, "/profissionais").permitAll()
      .requestMatchers(HttpMethod.POST, "/servicos").permitAll()
      .requestMatchers("/profissionais-servicos/**").permitAll()
      .anyRequest().permitAll()
    )
    // Retorna 401 (Unauthorized) ao invés de 302 (Redirect) para requisições não autenticadas, 
    // evitando bloqueios de CORS no cliente (React/Axios)
    .exceptionHandling(e -> e
      .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
    )
    .oauth2Login(oauth2 -> oauth2
      .userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService))
      .defaultSuccessUrl("http://localhost:5173", true)
    );
    
    return http.build();
  }
  
  /**
  * Habilita a comunicação segura com o frontend, liberando métodos HTTP (incluindo PATCH) e uso de credenciais/cookies de sessão.
  */
  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration config = new CorsConfiguration();
    config.setAllowedOrigins(Arrays.asList("http://localhost:5173"));
    config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
    config.setAllowedHeaders(Arrays.asList("*"));
    config.setAllowCredentials(true); 
    
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", config);
    return source;
  }
  
  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
    return config.getAuthenticationManager();
  }
}