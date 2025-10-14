package br.com.tick.tickdesck.models.user.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record UpdateUserDto(
        @NotBlank(message = "O NOME é obrigatório")
        String name,
        @Pattern(regexp = "\\S+", message = "O nome de usuário não pode conter espaços em branco")
        @NotBlank(message = "O USERNAME é obrigatório")
        String username,
        @NotBlank(message = "O EMAIL é obrigatório")
        String email,
        @NotNull(message = "A ROLE é obrigatória")
        Role role) {
}
