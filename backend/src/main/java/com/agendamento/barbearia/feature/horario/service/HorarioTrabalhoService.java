package com.agendamento.barbearia.feature.horario.service;

import com.agendamento.barbearia.feature.horario.dto.HorarioTrabalhoDTO;
import com.agendamento.barbearia.feature.horario.model.HorarioTrabalho;
import com.agendamento.barbearia.feature.horario.repo.HorarioTrabalhoRepository;
import com.agendamento.barbearia.feature.profissional.model.Profissional;
import com.agendamento.barbearia.feature.profissional.repo.ProfissionalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

/**
* Camada de serviço para a gestão da agenda e turnos de atendimento dos barbeiros.
*/
@Service
@RequiredArgsConstructor
public class HorarioTrabalhoService {
  
  private final HorarioTrabalhoRepository repo;
  private final ProfissionalRepository profissionalRepo;
  
  /**
  * Atualiza a escala de trabalho do barbeiro substituindo de forma atómica 
  * todos os turnos anteriores pelos novos configurados no painel.
  */
  @Transactional
  public void atualizarHorariosDoProfissional(Long profissionalId, List<HorarioTrabalhoDTO> horariosDTO) {
    Profissional profissional = profissionalRepo.findById(profissionalId)
    .orElseThrow(() -> new RuntimeException("Profissional não encontrado"));
    
    // Limpa a escala antiga para evitar duplicidades ou conflitos de chaves
    repo.deleteByProfissionalId(profissionalId);
    
    // Mapeia e converte o payload vindo do React para as entidades JPA
    List<HorarioTrabalho> novosHorarios = horariosDTO.stream().map(dto -> {
      HorarioTrabalho ht = new HorarioTrabalho();
      ht.setProfissional(profissional);
      ht.setDiaSemana(DayOfWeek.valueOf(dto.getDiaSemana()));
      ht.setHoraInicio(LocalTime.parse(dto.getHoraInicio()));
      ht.setHoraFim(LocalTime.parse(dto.getHoraFim()));
      return ht;
    }).toList();
    
    repo.saveAll(novosHorarios);
  }
}