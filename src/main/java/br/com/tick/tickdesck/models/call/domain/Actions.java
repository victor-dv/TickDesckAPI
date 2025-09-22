package br.com.tick.tickdesck.models.call.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
public class Actions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_chamado")
    private Long idChamado;

    @Column(name = "descricao_acao")
    private String Description;

    @Column(name = "status", nullable = false)
    private Boolean publica;

    @Column(name = "data_cadastro", nullable = false)
    private LocalDateTime dataCadastro;

    @ManyToOne
    @JoinColumn(name = "call_number", referencedColumnName = "callNumber")
    @JsonIgnore
    private CallsEntity call;


}

