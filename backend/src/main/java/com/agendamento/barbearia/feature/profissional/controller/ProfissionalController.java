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

/**
* Endpoint para gestão de profissionais (barbeiros), responsável pela listagem e cadastro.
*/
@RestController
@RequestMapping("/profissionais")
@RequiredArgsConstructor
public class ProfissionalController {
  
  private final RegistroProfissionalFacade facade;
  private final ProfissionalService profissionalService;
  
  /**
  * Registra um novo barbeiro utilizando o padrão Facade para orquestrar 
  * a criação simultânea do usuário de acesso e do perfil profissional.
  */
  @PostMapping
  public ResponseEntity<ProfissionalResponseDTO> criarBarbeiro(@RequestBody ProfissionalRequestDTO request) {
    ProfissionalResponseDTO response = facade.registrarNovoBarbeiro(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }
  
  /**
  * Retorna a lista de todos os barbeiros disponíveis na plataforma para exibição no frontend.
  */
  @GetMapping
  public ResponseEntity<List<ProfissionalResponseDTO>> listarBarbeiros() {
    return ResponseEntity.ok(profissionalService.listarTodos());
  }
}