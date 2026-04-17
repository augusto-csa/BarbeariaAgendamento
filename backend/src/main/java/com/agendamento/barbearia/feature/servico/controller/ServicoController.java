package com.agendamento.barbearia.feature.servico.controller;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.agendamento.barbearia.feature.servico.model.Servico;
import com.agendamento.barbearia.feature.servico.service.ServicoService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/servicos")
public class ServicoController {

    private final ServicoService service;

    // Rota GET: Retorna a lista de todos os serviços (http://localhost:8080/api/servicos)
    @GetMapping
    public List<Servico> listarTodos() {
        return service.findAll();
    }

    // Rota POST: Cria um novo serviço no banco
    @PostMapping
    public Servico criar(@RequestBody Servico servico) {
        return service.save(servico);
    }
}