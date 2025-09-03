package br.com.tick.tickdesck.application.dto;

import java.time.LocalDateTime;

public record CallRequestDto(
        int callNumber,
        String emailEnvio,
        String usernameEnvio,
        int urgencia,
        LocalDateTime previsaoSolucao,
        LocalDateTime dataFechamento
) {

}