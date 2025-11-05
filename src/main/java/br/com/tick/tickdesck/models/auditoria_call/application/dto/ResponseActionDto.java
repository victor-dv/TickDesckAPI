package br.com.tick.tickdesck.models.auditoria_call.application.dto;

import br.com.tick.tickdesck.models.auditoria_call.domain.ActionEntity;
import br.com.tick.tickdesck.models.files.application.dto.FileDto;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

public record  ResponseActionDto(
        Long id,
        String description,
        RoleStatusAction statusAction,
        LocalDateTime data,
        UserInfo user,
        CallInfo call,
        List<FileDto> files) {

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
                ),
                entity.getFile().stream().map(file -> {
                    Path path = Paths.get(file.getPath());
                    boolean exists = Files.exists(path);

                    if (!exists) {
                        file.setPath("Arquivo não encontrado");
                    }
                    return new FileDto(
                            file.getId(),
                            file.getName(),
                            file.getType(),
                            file.getPath()
                    );
                }).toList()
        );
    }

    public record UserInfo(Long id, String name, String username, String role) {}
    public record CallInfo(Long id, Integer numberCall, String title, String teamName, String urgency, Boolean status) {}
}

