package com.agendamento.barbearia.config; // Lembre-se de manter o seu pacote!

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
  
  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**") // O "/**" significa: para TODAS as rotas da API
    .allowedOrigins("http://localhost:5173") // A porta do nosso React com Vite
    .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD", "TRACE", "CONNECT", "PATCH") // Todos os verbos HTTP liberados
    .allowedHeaders("*"); // Permite que o React envie qualquer tipo de cabeçalho (como tokens de segurança no futuro)
  }
}