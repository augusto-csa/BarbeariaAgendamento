package com.agendamento.barbearia.feature.profissional_servico.repo;

import com.agendamento.barbearia.feature.profissional_servico.model.ProfissionalServico;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProfissionalServicoRepository extends JpaRepository<ProfissionalServico, Long> {
    List<ProfissionalServico> findByProfissionalId(Long profissionalId);
}