package br.com.tick.tickdesck.models.user.application.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateUserDto(
        @NotBlank(message = "O NOME é obrigatório")
        String name,
        @NotBlank(message = "O USERNAME é obrigatório")
        String username,
        @NotBlank(message = "O EMAIL é obrigatório")
        String email,
        @NotBlank(message = "A PASSWORD é obrigatória")
        String password,
        @NotBlank(message = "A ROLE é obrigatória")
        Role role) {
}
