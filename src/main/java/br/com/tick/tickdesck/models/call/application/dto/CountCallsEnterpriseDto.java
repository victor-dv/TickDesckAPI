package br.com.tick.tickdesck.models.call.application.dto;

public record CountCallsEnterpriseDto(
        Long total,
        Long totalAbertos,
        Long totalFechados
) {
}

