package com.agendamento.barbearia.feature.avaliacao.model;

import com.agendamento.barbearia.feature.profissional.model.Profissional;
import com.agendamento.barbearia.feature.usuario.model.Usuario;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "avaliacoes")
public class Avaliacao {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  
  @Column(nullable = false)
  private Integer nota;
  
  @Column(length = 500)
  private String comentario;
  
  @Column(nullable = false)
  private LocalDateTime dataAvaliacao = LocalDateTime.now();
  
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "profissional_id", nullable = false)
  private Profissional profissional;
  
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "cliente_id", nullable = false)
  private Usuario cliente;
}