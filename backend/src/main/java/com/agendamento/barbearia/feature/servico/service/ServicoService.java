package com.agendamento.barbearia.feature.servico.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.agendamento.barbearia.feature.servico.model.Servico;
import com.agendamento.barbearia.feature.servico.repo.ServicoRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ServicoService {
    
    private final ServicoRepository repo;

    public List<Servico> findAll() {
        return repo.findAll();
    }

    public Servico save(Servico servico) {
        return repo.save(servico);
    }
}
