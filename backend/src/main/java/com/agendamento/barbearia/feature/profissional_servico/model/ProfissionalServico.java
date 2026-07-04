package com.agendamento.barbearia.feature.profissional_servico.model;

import com.agendamento.barbearia.feature.profissional.model.Profissional;
import com.agendamento.barbearia.feature.servico.model.Servico;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "profissionais_servicos")
public class ProfissionalServico {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "profissional_id", nullable = false)
  private Profissional profissional;
  
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "servico_id", nullable = false)
  private Servico servico;
}