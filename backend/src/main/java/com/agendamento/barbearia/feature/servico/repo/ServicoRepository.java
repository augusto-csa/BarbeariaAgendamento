package com.agendamento.barbearia.feature.servico.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.agendamento.barbearia.feature.servico.model.Servico;

@Repository
public interface ServicoRepository extends JpaRepository<Servico, Long> {
    
}