package com.agendamento.barbearia.feature.agendamento.repo;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.agendamento.barbearia.feature.agendamento.model.Agendamento;

public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {
    List<Agendamento> findByClienteId(Long clienteId);

    @Query("SELECT DISTINCT a FROM Agendamento a LEFT JOIN FETCH a.servicos")
    List<Agendamento> findAllComServicos();

    @Query("SELECT DISTINCT a FROM Agendamento a LEFT JOIN FETCH a.servicos " +
           "WHERE a.profissional.id = :profissionalId " +
           "AND a.dataHora >= :inicioDia AND a.dataHora <= :fimDia " +
           "AND a.status != 'CANCELADO'")
    List<Agendamento> buscarAgendaDoBarbeiroNoDia(
        @Param("profissionalId") Long profissionalId, 
        @Param("inicioDia") LocalDateTime inicioDia, 
        @Param("fimDia") LocalDateTime fimDia
    );

    @Query("SELECT DISTINCT a FROM Agendamento a LEFT JOIN FETCH a.servicos WHERE a.profissional.id = :profissionalId ORDER BY a.dataHora ASC")
    List<Agendamento> findByProfissionalIdComServicos(@Param("profissionalId") Long profissionalId);
}