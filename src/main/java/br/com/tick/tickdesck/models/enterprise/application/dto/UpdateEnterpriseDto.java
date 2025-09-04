package br.com.tick.tickdesck.models.enterprise.application.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UpdateEnterpriseDto(
        @NotBlank(message = "O e-mail é obrigatório")
        @Email(message = "E-mail inválido")
        String email,

        @NotBlank( message = "O nome fantasia é obrigatório")
        String fantasyName,

        @NotBlank(message = "O telefone é obrigatório")
        String phone
) {
}
