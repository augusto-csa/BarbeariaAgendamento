package com.agendamento.barbearia.feature.profissional_servico.repo;

import com.agendamento.barbearia.feature.profissional_servico.model.ProfissionalServico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProfissionalServicoRepository extends JpaRepository<ProfissionalServico, Long> {
  @Query("SELECT ps FROM ProfissionalServico ps JOIN FETCH ps.servico WHERE ps.profissional.id = :profissionalId")
  List<ProfissionalServico> findByProfissionalId(@Param("profissionalId") Long profissionalId);
  void deleteByProfissionalId(Long profissionalId);
}