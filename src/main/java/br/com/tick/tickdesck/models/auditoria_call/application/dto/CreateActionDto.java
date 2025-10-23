package br.com.tick.tickdesck.models.auditoria_call.application.dto;

public record CreateActionDto (
        String description,
        Long userId,
        Long callId,
        RoleStatusAction statusAction
){


}
