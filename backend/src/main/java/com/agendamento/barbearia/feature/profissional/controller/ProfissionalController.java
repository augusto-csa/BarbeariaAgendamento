package com.agendamento.barbearia.feature.profissional.controller;

import com.agendamento.barbearia.feature.profissional.dto.ProfissionalRequestDTO;
import com.agendamento.barbearia.feature.profissional.dto.ProfissionalResponseDTO;
import com.agendamento.barbearia.feature.profissional.facade.RegistroProfissionalFacade;
import com.agendamento.barbearia.feature.profissional.service.ProfissionalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/profissionais")
@RequiredArgsConstructor
public class ProfissionalController {

    private final RegistroProfissionalFacade facade;
    private final ProfissionalService profissionalService;

    @PostMapping
    public ResponseEntity<ProfissionalResponseDTO> criarBarbeiro(@RequestBody ProfissionalRequestDTO request) {
        ProfissionalResponseDTO response = facade.registrarNovoBarbeiro(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<ProfissionalResponseDTO>> listarBarbeiros() {
        return ResponseEntity.ok(profissionalService.listarTodos());
    }
}