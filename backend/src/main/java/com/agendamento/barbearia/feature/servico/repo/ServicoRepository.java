package com.agendamento.barbearia.feature.servico.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import com.agendamento.barbearia.feature.servico.model.Servico;

public interface ServicoRepository extends JpaRepository<Servico, Long> {
  
}