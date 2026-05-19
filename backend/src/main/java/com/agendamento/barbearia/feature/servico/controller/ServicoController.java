package com.agendamento.barbearia.feature.servico.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.agendamento.barbearia.feature.servico.dto.ServicoRequestDTO;
import com.agendamento.barbearia.feature.servico.dto.ServicoResponseDTO;
import com.agendamento.barbearia.feature.servico.service.ServicoService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/servicos")
public class ServicoController {

    private final ServicoService service;

    @PostMapping
    public ResponseEntity<ServicoResponseDTO> criarServico(@RequestBody ServicoRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(request));
    }

    @GetMapping
    public ResponseEntity<List<ServicoResponseDTO>> listarServicos() {
        return ResponseEntity.ok(service.findAll());
    }
}