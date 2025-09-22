package br.com.tick.tickdesck.models.team.application.dto;

public record CreateTeamDto(
        String name,
        Long enterpriseId
) {
}
