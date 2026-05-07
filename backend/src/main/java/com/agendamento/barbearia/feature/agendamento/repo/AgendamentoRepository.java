package com.agendamento.barbearia.feature.agendamento.repo;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.agendamento.barbearia.feature.agendamento.model.Agendamento;

@Repository
public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {
    List<Agendamento> findByClienteId(Long clienteId);

    @Query("SELECT DISTINCT a FROM Agendamento a LEFT JOIN FETCH a.servicos")
    List<Agendamento> findAllComServicos();
}