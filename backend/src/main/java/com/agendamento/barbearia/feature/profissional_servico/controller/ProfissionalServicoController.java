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

/**
* Endpoint responsável por gerenciar a associação entre os profissionais (barbeiros) 
* e os serviços que eles estão habilitados a realizar.
*/
@RestController
@RequestMapping("/profissionais-servicos")
@RequiredArgsConstructor
public class ProfissionalServicoController {
  
  private final ProfissionalServicoService service;
  
  @PostMapping
  public ResponseEntity<ProfissionalServicoResponseDTO> vincular(@RequestBody ProfissionalServicoRequestDTO request) {
    return ResponseEntity.status(HttpStatus.CREATED).body(service.vincular(request));
  }
  
  /**
  * Retorna a lista de todos os serviços que um profissional específico realiza.
  */
  @GetMapping("/barbeiro/{profissionalId}")
  public ResponseEntity<List<ProfissionalServicoResponseDTO>> listarServicosDoBarbeiro(@PathVariable Long profissionalId) {
    return ResponseEntity.ok(service.buscarServicosDoBarbeiro(profissionalId));
  }
  
  /**
  * Atualiza os serviços oferecidos pelo profissional em lote (Bulk), 
  * apagando as configurações antigas e salvando a nova seleção de forma atômica.
  */
  @PostMapping("/vincular-em-lote/{profissionalId}")
  public ResponseEntity<Void> vincularServicos(@PathVariable Long profissionalId, @RequestBody List<Long> servicoIds) {
    service.atualizarServicosDoProfissional(profissionalId, servicoIds);
    return ResponseEntity.ok().build();
  }
}