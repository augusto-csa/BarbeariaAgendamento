package com.agendamento.barbearia.feature.horario.controller;

import com.agendamento.barbearia.feature.horario.dto.HorarioTrabalhoDTO;
import com.agendamento.barbearia.feature.horario.repo.HorarioTrabalhoRepository;
import com.agendamento.barbearia.feature.horario.service.HorarioTrabalhoService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/horarios")
@RequiredArgsConstructor
public class HorarioTrabalhoController {

    private final HorarioTrabalhoRepository repo;
    private final HorarioTrabalhoService service;

    @GetMapping("/barbeiro/{id}/dias")
    public ResponseEntity<List<String>> getDiasTrabalho(@PathVariable Long id) {
        // Busca os horários e extrai apenas o NOME do dia da semana, sem repetir
        List<String> dias = repo.findByProfissionalId(id).stream()
                .map(h -> h.getDiaSemana().name())
                .distinct()
                .toList();
                
        return ResponseEntity.ok(dias);
    }

    @PostMapping("/salvar-agenda/{profissionalId}")
    public ResponseEntity<Void> salvarAgenda(@PathVariable Long profissionalId, @RequestBody List<HorarioTrabalhoDTO> horarios) {
        service.atualizarHorariosDoProfissional(profissionalId, horarios);
        return ResponseEntity.ok().build();
    }
}