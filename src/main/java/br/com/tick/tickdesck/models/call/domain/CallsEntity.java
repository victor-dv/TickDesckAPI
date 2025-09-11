package br.com.tick.tickdesck.models.call.domain;

import jakarta.persistence.*;
import jakarta.websocket.OnMessage;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
public class CallsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int callNumber;
    @Column(name = "email_envio", nullable = false)
    private String emailEnvio;
    @Column(name = "username_envio", nullable = false)
    private String usernameEnvio;
    @Column(nullable = false)

    //status do chamado, se ele está ativo ou não
    private boolean status = true; // true = aberto, false = fechado

    //    id do requerente do chamado
    private Long idCliente;
    private Long idEquipe;
    private Long idUsuarioResponsavel;

    @Column(nullable = false)
    private int urgencia;

    @Column(name = "previsao_solucao")
    private LocalDate previsaoSolucao;
    @Column(name = "data_fechamento")
    private LocalDateTime dataFechamento;

    @Column(name = "usuario_fechamento")
    private Long usuarioFechamento;

}

