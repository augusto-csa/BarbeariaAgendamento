package com.agendamento.barbearia.feature.agendamento.controller;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.agendamento.barbearia.feature.agendamento.model.Agendamento;
import com.agendamento.barbearia.feature.agendamento.service.AgendamentoService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/agendamentos")
public class AgendamentoController {

    private final AgendamentoService service;

    @GetMapping
    public List<Agendamento> listarTodos() {
        return service.findAllComServicos();
    }

    @PostMapping
    public Agendamento criar(@RequestBody Agendamento agendamento) {
        // Por enquanto, salvamos diretamente. Num projeto real, teríamos validações aqui!
        agendamento.setStatus("PENDENTE");
        return service.criar(agendamento);
    }
}