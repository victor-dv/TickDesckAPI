package br.com.tick.tickdesck.config.email.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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
    private List<String> suggestedSolutions; // Soluções sugeridas para o problema
}
