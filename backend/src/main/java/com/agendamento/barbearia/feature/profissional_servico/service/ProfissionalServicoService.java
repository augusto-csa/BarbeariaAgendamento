package com.agendamento.barbearia.feature.profissional_servico.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.agendamento.barbearia.feature.profissional.model.Profissional;
import com.agendamento.barbearia.feature.profissional.repo.ProfissionalRepository;
import com.agendamento.barbearia.feature.profissional_servico.dto.ProfissionalServicoRequestDTO;
import com.agendamento.barbearia.feature.profissional_servico.dto.ProfissionalServicoResponseDTO;
import com.agendamento.barbearia.feature.profissional_servico.mapper.ProfissionalServicoMapper;
import com.agendamento.barbearia.feature.profissional_servico.model.ProfissionalServico;
import com.agendamento.barbearia.feature.profissional_servico.repo.ProfissionalServicoRepository;
import com.agendamento.barbearia.feature.servico.model.Servico;
import com.agendamento.barbearia.feature.servico.repo.ServicoRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProfissionalServicoService {

    private final ProfissionalServicoRepository repo;
    private final ProfissionalRepository profissionalRepository;
    private final ServicoRepository servicoRepository;
    private final ProfissionalServicoMapper mapper; 

    public ProfissionalServicoResponseDTO vincular(ProfissionalServicoRequestDTO request) {
        Profissional profissional = profissionalRepository.findById(request.getProfissionalId())
                .orElseThrow(() -> new RuntimeException("Barbeiro não encontrado"));
                
        Servico servico = servicoRepository.findById(request.getServicoId())
                .orElseThrow(() -> new RuntimeException("Serviço não encontrado"));

        ProfissionalServico vinculo = new ProfissionalServico();
        vinculo.setProfissional(profissional);
        vinculo.setServico(servico);

        return mapper.toResponseDTO(repo.save(vinculo));
    }

    // Método super útil para o React: Buscar todos os serviços de um barbeiro específico
    public List<ProfissionalServicoResponseDTO> buscarServicosDoBarbeiro(Long profissionalId) {
        return repo.findByProfissionalId(profissionalId)
                .stream()
                .map(mapper::toResponseDTO)
                .toList();
    }
}