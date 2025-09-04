package br.com.tick.tickdesck.models.call.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Range;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record CreateCallDto(

        int callNumber,
        @NotBlank(message = "É necessario o email de envio do chamado")
        String emailEnvio,

        @NotBlank(message = "É necessario o nome do usuário que está enviando o chamado")
        String usernameEnvio,

        @Range(min = 10, max = 100, message = "A urgência deve estar entre 10 e 100")
        int urgencia
) {

}