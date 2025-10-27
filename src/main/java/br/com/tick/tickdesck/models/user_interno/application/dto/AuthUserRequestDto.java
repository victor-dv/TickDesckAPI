package br.com.tick.tickdesck.models.user_interno.application.dto;

import jakarta.validation.constraints.NotBlank;

// DTO para a requisição de autenticação do usuário
public record AuthUserRequestDto (
        @NotBlank(message = "O EMAIL é obrigatório")
        String email,
        @NotBlank(message = "A PASSWORD é obrigatória")
        String password
) {
}
