package com.agendamento.barbearia.feature.profissional.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.agendamento.barbearia.feature.profissional.model.Profissional;

public interface ProfissionalRepository extends JpaRepository<Profissional, Long>{
  @Query("SELECT DISTINCT a FROM Profissional a LEFT JOIN FETCH a.avaliacoes")
  List<Profissional> findlAllComAgendamentos();
  Optional<Profissional> findByUsuarioId(Long usuarioId);
}
