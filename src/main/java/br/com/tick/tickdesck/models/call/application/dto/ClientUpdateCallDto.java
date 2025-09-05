package br.com.tick.tickdesck.models.call.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;

import java.time.LocalDate;

public record ClientUpdateCallDto(
        int callNumber,
        @NotBlank(message = "É necessario o email de envio do chamado")
        @Email(message = "Email inválido", regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")
        String emailEnvio,

        @NotBlank(message = "É necessario o nome do usuário que está enviando o chamado")
        String usernameEnvio,

        @Range(min = 1, max = 3, message = "A urgência deve estar entre 1 e 3")
        int urgencia,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
        LocalDate previsaoSolucao
) {

}