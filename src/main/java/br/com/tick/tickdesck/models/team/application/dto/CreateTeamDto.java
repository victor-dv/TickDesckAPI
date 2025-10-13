package br.com.tick.tickdesck.models.team.application.dto;

import jakarta.validation.constraints.Size;

public record CreateTeamDto(
        @Size(min = 5, max = 30, message = "O nome da equipe deve ter entre 5 e 30 caracteres")
        String name,
        Long enterpriseId
) {
}
