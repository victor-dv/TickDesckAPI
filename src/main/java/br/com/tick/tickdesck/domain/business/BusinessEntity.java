package br.com.tick.tickdesck.domain.business;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.validation.constraints.Email;
import lombok.Data;

@Entity
@Data
public class BusinessEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Email
    private String EmailEnvio;
    @Email
    private String EmailParaChamados;
    private  String RazaoSocial;
    private String NomeReduzido;
    private int QtdMaximaUsuarios;

}



