package com.agendamento.barbearia.feature.horario.repo;

import com.agendamento.barbearia.feature.horario.model.HorarioTrabalho;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.DayOfWeek;
import java.util.List;

public interface HorarioTrabalhoRepository extends JpaRepository<HorarioTrabalho, Long> {
  List<HorarioTrabalho> findByProfissionalIdAndDiaSemana(Long profissionalId, DayOfWeek diaSemana);
  List<HorarioTrabalho> findByProfissionalId(Long profissionalId);
  void deleteByProfissionalId(Long profissionalId);
}