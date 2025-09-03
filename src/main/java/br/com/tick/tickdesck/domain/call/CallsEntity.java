package br.com.tick.tickdesck.domain.call;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Entity
@Data
public class CallsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int callNumber;
    @Column(name = "email_envio", nullable = false)
    public String emailEnvio;
    @Column(name = "username_envio", nullable = false)
    public String usernameEnvio;

    private Long idEquipe;
    private Long idUsuarioResponsavel;

    @Column(nullable = false)
    private int urgencia;

    @Column(name = "previsao_solucao")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime previsaoSolucao;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "data_fechamento")
    private LocalDateTime dataFechamento;

    @Column(name = "usuario_fechamento")
    private Long usuarioFechamento;

}

