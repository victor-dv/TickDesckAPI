package br.com.tick.tickdesck.models.password.application.dto;

public record UpdatePasswordDto(
        String oldPassword,
        String newPassword
) {}
