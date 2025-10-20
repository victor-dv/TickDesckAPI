package br.com.tick.tickdesck.models.team.domain;

import br.com.tick.tickdesck.models.enterprise.domain.EnterpriseEntity;
import br.com.tick.tickdesck.models.user.domain.UserEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class TeamEntity {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enterpise_id", referencedColumnName = "id", nullable = false)
    @JsonBackReference
    private EnterpriseEntity enterprise;
    @Column(nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "teamEntity", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference // Ajuda a evitar recursão infinita ao converter para JSON
    private List<UserEntity> users = new ArrayList<>();
}
