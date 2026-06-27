package com.agendamento.barbearia.feature.agendamento.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.agendamento.barbearia.feature.agendamento.dto.AgendamentoRequestDTO;
import com.agendamento.barbearia.feature.agendamento.dto.AgendamentoResponseDTO;
import com.agendamento.barbearia.feature.agendamento.service.AgendamentoService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/agendamentos")
public class AgendamentoController {

    private final AgendamentoService service;

    @GetMapping
    public List<AgendamentoResponseDTO> listarTodos() {
        return service.findAllComServicos();
    }

    @PostMapping
    public AgendamentoResponseDTO criar(@RequestBody AgendamentoRequestDTO agendamento) {
        return service.create(agendamento);
    }

    @GetMapping("/disponibilidade")
    public ResponseEntity<List<String>> getDisponibilidade(
            @RequestParam Long profissionalId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
        
        return ResponseEntity.ok(service.buscarHorariosDisponiveis(profissionalId, data));
    }

    @GetMapping("/barbeiro/{profissionalId}")
    public List<AgendamentoResponseDTO> listarAgendaBarbeiro(@PathVariable Long profissionalId) {
        return service.buscarAgendaDoBarbeiro(profissionalId);
    }
}