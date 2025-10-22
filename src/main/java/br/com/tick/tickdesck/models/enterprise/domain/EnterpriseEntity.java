package br.com.tick.tickdesck.models.enterprise.domain;

import br.com.tick.tickdesck.models.team.domain.TeamEntity;
import br.com.tick.tickdesck.models.user.domain.UserEntity;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "enterprises")
public class EnterpriseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    @Email(message = "O email deve ser válido")
    private String email;
    private String corporateName;
    private String fantasyName;
    private String cnpj;
    private String phone;

    @OneToMany(mappedBy = "enterprise", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<TeamEntity> teams;
}
