package br.com.tick.tickdesck.models.call.application.dto;

public record UpdateCallDto(
        String title,
        boolean status,
        UrgenciaCallDto urgency
) {
}
