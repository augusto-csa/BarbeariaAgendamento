package com.agendamento.barbearia.feature.horario.controller;

import com.agendamento.barbearia.feature.horario.dto.HorarioTrabalhoDTO;
import com.agendamento.barbearia.feature.horario.repo.HorarioTrabalhoRepository;
import com.agendamento.barbearia.feature.horario.service.HorarioTrabalhoService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
* Endpoint para gestão da escala semanal de horários dos profissionais.
*/
@RestController
@RequestMapping("/horarios")
@RequiredArgsConstructor
public class HorarioTrabalhoController {
  
  private final HorarioTrabalhoRepository repo;
  private final HorarioTrabalhoService service;
  
  /**
  * Retorna a lista de dias da semana em que o profissional atende (sem repetições).
  */
  @GetMapping("/barbeiro/{id}/dias")
  public ResponseEntity<List<String>> getDiasTrabalho(@PathVariable Long id) {
    List<String> dias = repo.findByProfissionalId(id).stream()
    .map(h -> h.getDiaSemana().name())
    .distinct()
    .toList();
    
    return ResponseEntity.ok(dias);
  }
  
  /**
  * Atualiza a escala completa de trabalho do profissional em lote (Bulk).
  */
  @PostMapping("/salvar-agenda/{profissionalId}")
  public ResponseEntity<Void> salvarAgenda(@PathVariable Long profissionalId, @RequestBody List<HorarioTrabalhoDTO> horarios) {
    service.atualizarHorariosDoProfissional(profissionalId, horarios);
    return ResponseEntity.ok().build();
  }
}