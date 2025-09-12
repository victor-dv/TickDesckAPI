package br.com.tick.tickdesck.models.call.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.Range;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record CreateCallDto(

        int callNumber,
        @NotNull(message = "É necessário o id do requerente do chamado")
        Long idCliente,
        @NotNull(message = "É necessário o id da empresa")
        Long idEmpresa,
        @NotNull(message = "É necessário o id da empresa")
        long idEquipe,
        @NotBlank(message = "É necessário um email de envio do chamado")
        @Email(message = "Email inválido", regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")
        String emailEnvio,

        @NotBlank(message = "É necessário o nome do usuário que está enviando o chamado")
        String usernameEnvio,

        @Range(min = 1, max = 3, message = "A urgência deve estar entre 1 e 3")
        int urgencia,

        @Column(name = "Status", nullable = false)
        boolean status,


        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
        LocalDate previsaoSolucao
) {

}



