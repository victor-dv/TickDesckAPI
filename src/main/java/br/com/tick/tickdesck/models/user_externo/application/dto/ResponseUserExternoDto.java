package br.com.tick.tickdesck.models.user_externo.application.dto;

import br.com.tick.tickdesck.models.user_externo.domain.UserExternoEntity;

public record ResponseUserExternoDto(
        Long id,
        String name,
        String email
) {
    public static ResponseUserExternoDto fromUserExternoEntity(UserExternoEntity userExternoEntity) {
        return new ResponseUserExternoDto(
                userExternoEntity.getId(),
                userExternoEntity.getName(),
                userExternoEntity.getEmail()
        );
    }
}

