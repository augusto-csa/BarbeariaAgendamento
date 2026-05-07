package com.agendamento.barbearia.feature.agendamento.controller;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
}