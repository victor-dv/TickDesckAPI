package br.com.tick.tickdesck.models.team.domain;

import br.com.tick.tickdesck.models.enterprise.domain.EnterpriseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
public class TeamEntity {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "enterpise_id", referencedColumnName = "id", nullable = false)
    private EnterpriseEntity enterprise;
    @Size(min = 5, max = 30, message = "O nome da equipe deve ter entre 5 e 30 caracteres")
    @Column(nullable = false, unique = true)
    private String name;
}
