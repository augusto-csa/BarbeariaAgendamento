package com.agendamento.barbearia.feature.profissional.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agendamento.barbearia.feature.profissional.model.Profissional;

public interface ProfissionalRepository extends JpaRepository<Profissional, Long>{
    
}
