package br.com.tick.tickdesck.models.enterprise.application.dto;

import br.com.tick.tickdesck.models.enterprise.domain.EnterpriseEntity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ResponseEnterpriseDto(
        Long id,
        String email,
        String corporateName,
        String fantasyName,
        String phone,
        String cnpj
) {
    public static ResponseEnterpriseDto fromEnterpriseEntity(EnterpriseEntity enterpriseEntity) {
        return new ResponseEnterpriseDto(
                enterpriseEntity.getId(),
                enterpriseEntity.getEmail(),
                enterpriseEntity.getCorporateName(),
                enterpriseEntity.getFantasyName(),
                enterpriseEntity.getPhone(),
                enterpriseEntity.getCnpj()
        );
    }
}
