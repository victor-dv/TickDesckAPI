package br.com.tick.tickdesck.models.call.application.dto;

public record   CreateCallDto (
        String title,
        Long userExternoId,
        Long userResponsavelId,
        boolean status,
        Long teamId,
        UrgenciaCallDto urgency
) {
}
