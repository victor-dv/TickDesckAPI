package br.com.tick.tickdesck.models.call.application.dto;

import br.com.tick.tickdesck.models.call.domain.CallsEntity;
import br.com.tick.tickdesck.models.team.application.dto.ResponseTeamDto;
import br.com.tick.tickdesck.models.user_interno.application.dto.ResponseUserDto;

import java.time.LocalDateTime;

public record ResponseCallDto(
        Long id,
        Integer numberCall,
        String title,
        Long requisitanteId,
        ResponseUserDto userResponsavel,
        boolean status,
        ResponseTeamDto team,
        UrgenciaCallDto urgency,
        LocalDateTime previsaoSolucao,
        LocalDateTime dataAbertura
) {
    public static ResponseCallDto fromCallEntity(CallsEntity call) {

        ResponseUserDto userDto = call.getUserResponsavel() != null
                ? ResponseUserDto.fromUser(call.getUserResponsavel())
                : null;
        ResponseTeamDto teamDto = call.getTeam() != null
                ? ResponseTeamDto.fromTeamEntity(call.getTeam())
                : null;

        return new ResponseCallDto(
                call.getId(),
                call.getNumberCall(),
                call.getTitle(),
                call.getRequisitante().getId(),
                userDto,
                call.isStatus(),
                teamDto,
                call.getUrgencia(),
                call.getPrevisaoSolucao(),
                call.getDataAbertura()
        );
    }
}
