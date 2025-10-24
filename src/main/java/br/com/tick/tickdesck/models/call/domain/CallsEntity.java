package br.com.tick.tickdesck.models.call.domain;

import br.com.tick.tickdesck.models.call.application.dto.UrgenciaCallDto;
import br.com.tick.tickdesck.models.team.domain.TeamEntity;
import br.com.tick.tickdesck.models.user.domain.UserEntity;
import br.com.tick.tickdesck.models.user.domain.UserExternoEntity;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
public class CallsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false) // Boas práticas para um identificador
    private Integer numberCall;

    private String title;

    //Classe para referencia ao usuario que vem do email
    //Obtemos apenas os valores de email e username
    @ManyToOne
    @JoinColumn(name = "userExterno_id", referencedColumnName = "id", nullable = false)
    private UserExternoEntity userExterno;

    @ManyToOne
    @JoinColumn(name = "team_id", referencedColumnName = "id", nullable = false)
    private TeamEntity team;

    @Column(nullable = false)
    private boolean status = true;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = true)
    private UserEntity userResponsavel;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UrgenciaCallDto urgencia = UrgenciaCallDto.BAIXA;

    @Column(name = "previsao_solucao")
    private LocalDateTime previsaoSolucao;

    @CreationTimestamp
    @Column(name = "data_abertura", nullable = false, updatable = false)
    private LocalDateTime dataAbertura;

    @UpdateTimestamp
    @Column(name = "data_atualizacao", nullable = false)
    private LocalDateTime dataAtualizacao;


}

