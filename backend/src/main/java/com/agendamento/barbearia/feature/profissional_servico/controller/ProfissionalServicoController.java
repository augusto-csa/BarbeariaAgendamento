package com.agendamento.barbearia.feature.profissional_servico.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.agendamento.barbearia.feature.profissional_servico.dto.ProfissionalServicoRequestDTO;
import com.agendamento.barbearia.feature.profissional_servico.dto.ProfissionalServicoResponseDTO;
import com.agendamento.barbearia.feature.profissional_servico.service.ProfissionalServicoService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/profissionais-servicos")
@RequiredArgsConstructor
public class ProfissionalServicoController {

    private final ProfissionalServicoService service;

    // POST: Cria o vínculo
    @PostMapping
    public ResponseEntity<ProfissionalServicoResponseDTO> vincular(@RequestBody ProfissionalServicoRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.vincular(request));
    }

    // GET: Traz os serviços de um barbeiro (Ex: /profissionais-servicos/barbeiro/1)
    @GetMapping("/barbeiro/{profissionalId}")
    public ResponseEntity<List<ProfissionalServicoResponseDTO>> listarServicosDoBarbeiro(@PathVariable Long profissionalId) {
        return ResponseEntity.ok(service.buscarServicosDoBarbeiro(profissionalId));
    }
    
    // No futuro, você pode adicionar o DELETE aqui para remover um vínculo!
}