package br.com.tick.tickdesck.application.dto;

// DTO para a requisição de autenticação do usuário
public record AuthUserRequestDto (
    String email,
    String password) {
}
