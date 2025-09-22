package br.com.tick.tickdesck.models.user.application.dto;

import br.com.tick.tickdesck.models.user.domain.UserEntity;

public record ResponseUserDto(
        Long id,
        String name,
        String username,
        String email,
        Role role
) {
    public static ResponseUserDto fromUser(UserEntity userEntity) {
        return new ResponseUserDto(
                userEntity.getId(),
                userEntity.getName(),
                userEntity.getUsername(),
                userEntity.getEmail(),
                userEntity.getRole()
        );
    }
}
