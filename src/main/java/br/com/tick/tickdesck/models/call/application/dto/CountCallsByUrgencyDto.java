package br.com.tick.tickdesck.models.call.application.dto;

public record CountCallsByUrgencyDto(
        UrgenciaCallDto urgency,
        Long totalChamados
) {
}

