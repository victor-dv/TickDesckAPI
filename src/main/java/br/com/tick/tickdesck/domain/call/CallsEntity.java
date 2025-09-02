package br.com.tick.tickdesck.domain.call.call;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Data
public class CallsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private int NumeroChamado;
    @Email
    private String EmailEnvio;
    private String UsernameEnvio;
    private Long IdEquipe;
    private Long IdUsuarioResponsavel;
    private int Urgencia;

    @DateTimeFormat
    private int PrevisaoSolucao;
    @DateTimeFormat
    private int DataFechamento;

    private Long UsuarioFechamento;


}
