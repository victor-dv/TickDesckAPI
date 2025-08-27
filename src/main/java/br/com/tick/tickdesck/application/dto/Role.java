package br.com.tick.tickdesck.application.dto;

// Enum para representar os tipos de usuários que podem se registrar no sistema
// ADMIN: Usuário administrador com permissões completas
// SUPORT: Usuário de suporte com permissões limitadas
// GERENT: Usuário gerente com permissões intermediárias
// CLIENT: Usuário cliente com permissões básicas
public enum Role {
    ADMIN,
    SUPORT,
    GERENT,
    CLIENT
}
