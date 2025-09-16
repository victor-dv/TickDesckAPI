package br.com.tick.tickdesck.models.team.application.dto;

import br.com.tick.tickdesck.models.enterprise.application.dto.ResponseEnterpriseDto;
import br.com.tick.tickdesck.models.enterprise.application.dto.ResponseEnterpriseForTeamDto;
import br.com.tick.tickdesck.models.team.domain.TeamEntity;

public record ResponseTeamDto(
        Long id,
        String name,
        ResponseEnterpriseForTeamDto enterpriseDto
) {

    public  static  ResponseTeamDto fromTeamEntity(TeamEntity teamEntity){
        return new ResponseTeamDto(
                teamEntity.getId(),
                teamEntity.getName(),
                new ResponseEnterpriseForTeamDto(
                        teamEntity.getEnterprise().getId(),
                        teamEntity.getEnterprise().getFantasyName()
                )
        );
    }
}
