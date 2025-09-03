package br.com.tick.tickdesck.models.enterprise.application.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateEnterpriseDto(
        @NotBlank(message = "O e-mail é obrigatório")
        @Email(message = "E-mail inválido")
        String email,

        @NotBlank(message = "A razão social é obrigatória")
        String corporateName,

        @NotBlank(message = "O nome fantasia é obrigatório")
        String fantasyName,

        @NotBlank(message = "O CNPJ é obrigatório")
        @Size(min = 14, max = 14, message = "O CNPJ deve ter 14 caracteres")
        String cnpj,

        @NotBlank(message = "O telefone é obrigatório")
        String phone
) {
}
