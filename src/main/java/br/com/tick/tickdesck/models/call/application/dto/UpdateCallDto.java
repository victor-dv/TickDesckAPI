package br.com.tick.tickdesck.models.call.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record UpdateCallDto(
        int callNumber,
        @Range(min = 1, max = 3, message = "A urgência deve estar entre 10 e 100")
        int urgencia,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss dd-MM-yyyy")
        LocalDateTime dataFechamento,
        Long usuarioFechamento

) {


}
