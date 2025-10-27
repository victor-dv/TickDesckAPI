package br.com.tick.tickdesck.models.user_interno.application.dto;

import br.com.tick.tickdesck.models.team.application.dto.ResponseTeamDto;
import br.com.tick.tickdesck.models.user_interno.domain.UserEntity;

public record ResponseUserDto(
        Long id,
        String name,
        String username,
        String email,
        Role role,
        ResponseTeamDto team
) {
    public static ResponseUserDto fromUser(UserEntity userEntity) {
        ResponseTeamDto teamDto = userEntity.getTeamEntity() != null
                ? ResponseTeamDto.fromTeamEntity(userEntity.getTeamEntity())
                : null;

        return new ResponseUserDto(
                userEntity.getId(),
                userEntity.getName(),
                userEntity.getUsername(),
                userEntity.getEmail(),
                userEntity.getRole(),
                teamDto
        );
    }
}
