package com.agendamento.barbearia.feature.agendamento.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
public class AgendamentoResponseDTO {
    private Long id;
    private LocalDateTime dataHora;
    private String status;
    private ClienteDTO cliente;
    private ProfissionalDTO profissional;
    private List<ServicoDTO> servicos;

    // Sub-classes estáticas para formatar o JSON aninhado igual ao Frontend
    @Data
    public static class ClienteDTO {
        private String nome;
        private String email;
    }

    @Data
    public static class ProfissionalDTO {
        private UsuarioDTO usuario;
        
        @Data
        public static class UsuarioDTO {
            private String nome;
        }
    }

    @Data
    public static class ServicoDTO {
        private Long id;
        private String nome;
        private java.math.BigDecimal preco;
        private Integer duracaoMinutos;
    }
}