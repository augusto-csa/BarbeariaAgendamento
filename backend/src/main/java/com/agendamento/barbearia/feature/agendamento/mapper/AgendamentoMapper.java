package com.agendamento.barbearia.feature.agendamento.mapper;

import com.agendamento.barbearia.feature.agendamento.dto.AgendamentoResponseDTO;
import com.agendamento.barbearia.feature.agendamento.model.Agendamento;
import org.springframework.stereotype.Component;
import java.util.stream.Collectors;

@Component
public class AgendamentoMapper {

    public AgendamentoResponseDTO toResponseDTO(Agendamento entity) {
        AgendamentoResponseDTO dto = new AgendamentoResponseDTO();
        dto.setId(entity.getId());
        dto.setDataHora(entity.getDataHora());
        dto.setStatus(entity.getStatus());

        // Mapeando Cliente
        AgendamentoResponseDTO.ClienteDTO clienteDTO = new AgendamentoResponseDTO.ClienteDTO();
        clienteDTO.setNome(entity.getCliente().getNome());
        clienteDTO.setEmail(entity.getCliente().getEmail());
        dto.setCliente(clienteDTO);

        // Mapeando Profissional
        AgendamentoResponseDTO.ProfissionalDTO profissionalDTO = new AgendamentoResponseDTO.ProfissionalDTO();
        AgendamentoResponseDTO.ProfissionalDTO.UsuarioDTO profUsuarioDTO = new AgendamentoResponseDTO.ProfissionalDTO.UsuarioDTO();
        profUsuarioDTO.setNome(entity.getProfissional().getUsuario().getNome());
        profissionalDTO.setUsuario(profUsuarioDTO);
        dto.setProfissional(profissionalDTO);

        // Mapeando Serviços
        dto.setServicos(entity.getServicos().stream().map(servico -> {
            AgendamentoResponseDTO.ServicoDTO servicoDTO = new AgendamentoResponseDTO.ServicoDTO();
            servicoDTO.setId(servico.getId());
            servicoDTO.setNome(servico.getNome());
            servicoDTO.setPreco(servico.getPreco());
            servicoDTO.setDuracaoMinutos(servico.getDuracaoMinutos());
            return servicoDTO;
        }).collect(Collectors.toList()));

        return dto;
    }
}