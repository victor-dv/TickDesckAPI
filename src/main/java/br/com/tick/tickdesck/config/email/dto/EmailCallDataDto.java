package br.com.tick.tickdesck.config.email.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailCallDataDto {
    private String title;
    private String urgency; // "BAIXA", "MEDIA", "ALTA"
    private String description;
    private String requisitanteEmail;
    private String requisitanteName;
}
