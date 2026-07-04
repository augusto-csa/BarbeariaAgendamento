package com.agendamento.barbearia.feature.usuario.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.agendamento.barbearia.feature.profissional.dto.ProfissionalRequestDTO;
import com.agendamento.barbearia.feature.profissional.model.Profissional;
import com.agendamento.barbearia.feature.profissional.repo.ProfissionalRepository;
import com.agendamento.barbearia.feature.usuario.dto.UsuarioRequestDTO;
import com.agendamento.barbearia.feature.usuario.dto.UsuarioResponseDTO;
import com.agendamento.barbearia.feature.usuario.mapper.UsuarioMapper;
import com.agendamento.barbearia.feature.usuario.model.Usuario;
import com.agendamento.barbearia.feature.usuario.repo.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UsuarioService {
    
    private final UsuarioRepository repo;
    private final ProfissionalRepository profissionalRepo;
    private final UsuarioMapper mapper;
    private final PasswordEncoder passwordEncoder;

    public List<UsuarioResponseDTO> findAll() {
        return repo.findAll()
        .stream()
        .map(mapper::toResponse)
        .collect(Collectors.toList());
    }

    public UsuarioResponseDTO create(UsuarioRequestDTO request) {
        if (repo.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email já cadastrado");
        }
        
        Usuario usuario = new Usuario();
        usuario.setNome(request.getNome());
        usuario.setEmail(request.getEmail());
        usuario.setSenha(passwordEncoder.encode(request.getSenha()));
        
        if ("BARBEIRO".equalsIgnoreCase(request.getRole())) {
            usuario.setRole("BARBEIRO");
        } else {
            usuario.setRole("CLIENTE");
        }
        
        Usuario salvo = repo.save(usuario);

        if ("BARBEIRO".equals(salvo.getRole())) {
            Profissional profissional = new Profissional();
            profissional.setUsuario(salvo);
            profissional.setAtivo(true);
            profissionalRepo.save(profissional);
        }
        
        return mapper.toResponse(salvo);
    }

    public Usuario criarContaBarbeiro(ProfissionalRequestDTO request) {
        Usuario u = new Usuario();
        u.setNome(request.getNome());
        u.setEmail(request.getEmail());
        u.setFotoUrl(request.getFotoUrl());
        u.setSenha(passwordEncoder.encode(request.getSenha()));
        u.setRole("BARBEIRO");
        return repo.save(u);
    }

}
