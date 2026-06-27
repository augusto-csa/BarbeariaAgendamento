package com.agendamento.barbearia.feature.horario.controller;

import com.agendamento.barbearia.feature.horario.repo.HorarioTrabalhoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/horarios")
@RequiredArgsConstructor
public class HorarioTrabalhoController {

    private final HorarioTrabalhoRepository repo;

    @GetMapping("/barbeiro/{id}/dias")
    public ResponseEntity<List<String>> getDiasTrabalho(@PathVariable Long id) {
        // Busca os horários e extrai apenas o NOME do dia da semana, sem repetir
        List<String> dias = repo.findByProfissionalId(id).stream()
                .map(h -> h.getDiaSemana().name())
                .distinct()
                .toList();
                
        return ResponseEntity.ok(dias);
    }
}