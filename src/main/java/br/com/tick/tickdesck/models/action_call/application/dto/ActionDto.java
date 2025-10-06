package br.com.tick.tickdesck.models.action_call.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;
import java.util.List;

public record ActionDto (

        Long callNumber,
        Long id,
        @Length(max = 2500, message = "A descrição do problema deve ter no máximo 2500 caracteres")
        String description,
        Boolean publica,
        @JsonFormat(pattern = "dd-M-yyyy HH-mm-ss")
        LocalDateTime dataCadastro,
        List<FileDto>files


)
{

}
