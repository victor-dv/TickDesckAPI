package br.com.tick.tickdesck.models.auditoria_call.application.dto;

import br.com.tick.tickdesck.models.auditoria_call.domain.ActionEntity;

import java.time.LocalDateTime;

public record  ResponseActionDto(
        Long id,
        String description,
        RoleStatusAction statusAction,
        LocalDateTime data,
        UserInfo user,
        CallInfo call
) {

    public static ResponseActionDto fromEntity(ActionEntity entity) {
        return new ResponseActionDto(
                entity.getId(),
                entity.getDescription(),
                entity.getStatusAction(),
                entity.getData(),
                new UserInfo(
                        entity.getUser().getId(),
                        entity.getUser().getName(),
                        entity.getUser().getUsername(),
                        entity.getUser().getRole().toString()
                ),
                new CallInfo(
                        entity.getCallsEntity().getId(),
                        entity.getCallsEntity().getNumberCall(),
                        entity.getCallsEntity().getTitle(),
                        entity.getCallsEntity().getTeam().getName(),
                        entity.getCallsEntity().getUrgencia().toString(),
                        entity.getCallsEntity().isStatus()
                )
        );
    }

    public record UserInfo(Long id, String name, String username, String role) {}
    public record CallInfo(Long id, Integer numberCall, String title, String teamName, String urgency, Boolean status) {}
}

