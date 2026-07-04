package com.agendamento.barbearia.feature.horario.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class HorarioTrabalhoDTO {
    @Schema(example = "MONDAY")
    private String diaSemana;
    @Schema(example = "08:00:00")
    private String horaInicio;
    @Schema(example = "18:00:00")
    private String horaFim;
}