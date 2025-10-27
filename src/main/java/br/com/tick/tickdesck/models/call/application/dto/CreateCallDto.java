package br.com.tick.tickdesck.models.call.application.dto;

import java.time.LocalDateTime;

public record  CreateCallDto (
        Integer numberCall,
        String title,
        Long requisitanteId,
        Long userResponsavelId,
        boolean status,
        Long teamId,
        UrgenciaCallDto urgency,
        LocalDateTime dataAbertura,
        LocalDateTime previsaoSolucao

) {
}
