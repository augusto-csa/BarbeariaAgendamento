package com.agendamento.barbearia.feature.usuario.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "usuarios")
public class Usuario {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  
  @Column(nullable = false)
  private String nome;
  
  @Column(unique = true, nullable = false)
  private String email;
  
  private String telefone;
  
  @Column(nullable = false)
  private String senha;
  
  @Column(nullable = false)
  private String role; // CLIENTE, BARBEIRO, ADMIN

  @Column(length = 1000, name = "foto_url")
  private String fotoUrl;
}