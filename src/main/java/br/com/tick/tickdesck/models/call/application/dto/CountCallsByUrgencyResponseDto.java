package br.com.tick.tickdesck.models.call.application.dto;

import java.util.List;

public record CountCallsByUrgencyResponseDto(
        List<CountCallsByUrgencyDto> urgencias
) {
}

