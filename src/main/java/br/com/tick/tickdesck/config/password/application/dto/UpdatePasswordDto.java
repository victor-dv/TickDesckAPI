package br.com.tick.tickdesck.config.password.application.dto;

public record UpdatePasswordDto(
        String oldPassword,
        String newPassword
) {}
