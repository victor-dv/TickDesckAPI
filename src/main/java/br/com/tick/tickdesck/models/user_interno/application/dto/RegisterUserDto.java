package br.com.tick.tickdesck.models.user_interno.application.dto;

public record RegisterUserDto(
        String name,
        String username,
        String email,
        String password,
        Role role,
        Long teamId
) {
}
