package com.agendamento.barbearia.feature.usuario.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
    private final UsuarioMapper mapper;
    private final PasswordEncoder passwordEncoder;

    public List<UsuarioResponseDTO> findAll() {
        return repo.findAll()
        .stream()
        .map(mapper::toResponse)
        .collect(Collectors.toList());
    }

    public UsuarioResponseDTO create(UsuarioRequestDTO request){
        Usuario user = mapper.toEntity(request);
        String senhaCriptografada = passwordEncoder.encode(request.getSenha());
        user.setSenha(senhaCriptografada);
        user = repo.save(user);
        return mapper.toResponse(user);
    }

}
