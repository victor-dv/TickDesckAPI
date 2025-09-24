package br.com.tick.tickdesck.models.call.domain;

import br.com.tick.tickdesck.models.action_call.domain.Actions;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    private boolean status = true;

    @Column(name = "id_empresa", nullable = false)
    private Long idEmpresa;
    //    id do requerente do chamado
    @Column(name = "id_cliente", nullable = false)
    private Long idCliente;
    @Column(name = "id_equipe", nullable = false)
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

    @OneToMany(mappedBy = "call", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Actions> acoes = new ArrayList<>();



}

