package br.com.tick.tickdesck.models.call.application.dto;

import br.com.tick.tickdesck.models.team.application.dto.ResponseTeamDto;

import java.time.LocalDateTime;

public record UpdateCallDto(
        String title,
        boolean status,
        UrgenciaCallDto urgency,
        Long userResponsavelId,
        LocalDateTime previsaoSolucao,
        Long teamId
) {
}
