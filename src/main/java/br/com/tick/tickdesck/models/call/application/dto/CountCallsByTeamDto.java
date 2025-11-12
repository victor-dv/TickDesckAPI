package br.com.tick.tickdesck.models.call.application.dto;

public record CountCallsByTeamDto(
        Long teamId,
        String teamName,
        Long totalChamados
) {
}

