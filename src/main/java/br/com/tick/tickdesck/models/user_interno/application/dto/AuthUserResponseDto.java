package br.com.tick.tickdesck.models.user_interno.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


// DTO para a resposta de autenticação do usuário
@NoArgsConstructor
@Data
@Builder
@AllArgsConstructor
public class AuthUserResponseDto {
    private String access_token;
    private Long expires_in;
    private String message;

}
