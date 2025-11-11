package br.com.tick.tickdesck.models.requisitantes.application.dto;

import br.com.tick.tickdesck.models.requisitantes.domain.RequisitanteEntity;
import br.com.tick.tickdesck.models.user_externo.application.dto.ResponseUserExternoDto;
import br.com.tick.tickdesck.models.user_externo.domain.UserExternoEntity;

public record ResponseRequisitanteDto(
        Long id,
        String nome,
        String email
) {
    public static ResponseRequisitanteDto fromUserExternoEntity(RequisitanteEntity requisitanteEntity) {
        return new ResponseRequisitanteDto(
                requisitanteEntity.getId(),
                requisitanteEntity.getName(),
                requisitanteEntity.getEmail()
        );
    }
}