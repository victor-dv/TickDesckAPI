package br.com.tick.tickdesck.models.call.application.actiondto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record ActionDto (

        @Length(max = 2500, message = "A descrição do problema deve ter no máximo 2500 caracteres")
        String description,
        Boolean publica,
        @JsonFormat(pattern = "dd-M-yyyy HH-mm-ss")
        LocalDateTime dataCadastro

)
{

}
