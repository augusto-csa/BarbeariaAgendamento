package com.agendamento.barbearia.feature.profissional.service;

import com.agendamento.barbearia.feature.profissional.dto.ProfissionalRequestDTO;
import com.agendamento.barbearia.feature.profissional.dto.ProfissionalResponseDTO;
import com.agendamento.barbearia.feature.profissional.mapper.ProfissionalMapper;
import com.agendamento.barbearia.feature.profissional.model.Profissional;
import com.agendamento.barbearia.feature.profissional.repo.ProfissionalRepository;
import com.agendamento.barbearia.feature.usuario.model.Usuario;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfissionalService {

    private final ProfissionalRepository repository;
    private final ProfissionalMapper mapper;

    public List<ProfissionalResponseDTO> listarTodos() {
        return repository.findlAllComAgendamentos()
                .stream()
                .map(mapper::toResponseDTO)
                .toList();
    }

    public ProfissionalResponseDTO buscarPorId(Long id) {
        Profissional profissional = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Profissional não encontrado"));
                
        return mapper.toResponseDTO(profissional);
    }

    // Dentro do ProfissionalService
    public ProfissionalResponseDTO criarPerfilProfissional(ProfissionalRequestDTO request, Usuario usuario) {
        Profissional p = new Profissional();
        p.setBiografia(request.getBiografia());
        p.setUsuario(usuario);
        Profissional salvo = repository.save(p);
        return mapper.toResponseDTO(salvo);
    }
}