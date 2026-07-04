package com.agendamento.barbearia.feature.servico.model;

import java.math.BigDecimal;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "servicos")
public class Servico {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  
  @Column(nullable = false, length = 100)
  private String nome;
  
  @Column(length = 255)
  private String descricao;
  
  @Column(nullable = false, precision = 10, scale = 2)
  private BigDecimal preco;
  
  @Column(name = "duracao_minutos", nullable = false)
  private Integer duracaoMinutos;
}