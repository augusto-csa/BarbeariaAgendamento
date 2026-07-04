package com.agendamento.barbearia.feature.agendamento.model;

import java.time.LocalDateTime;
import java.util.List;

import com.agendamento.barbearia.feature.profissional.model.Profissional;
import com.agendamento.barbearia.feature.servico.model.Servico;
import com.agendamento.barbearia.feature.usuario.model.Usuario;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "agendamentos")
public class Agendamento {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "cliente_id", nullable = false)
  private Usuario cliente;
  
  @ManyToOne
  @JoinColumn(name = "profissional_id", nullable = false)
  private Profissional profissional;
  
  @Column(name = "data_hora", nullable = false)
  private LocalDateTime dataHora;
  
  @Column(nullable = false)
  private String status;
  
  @ManyToMany
  @JoinTable(
    name = "agendamentos_servicos",
    joinColumns = @JoinColumn(name = "agendamento_id"),
    inverseJoinColumns = @JoinColumn(name = "servico_id")
  )
  private List<Servico> servicos;
}