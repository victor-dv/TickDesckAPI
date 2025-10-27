package br.com.tick.tickdesck.models.user_interno.domain;

import br.com.tick.tickdesck.models.requisitantes.domain.RequisitanteEntity;
import br.com.tick.tickdesck.models.team.domain.TeamEntity;
import br.com.tick.tickdesck.models.user_interno.application.dto.Role;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
// import jakarta.validation.constraints.Email; // <-- REMOVA ESTE IMPORT
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "usuarios_internos")
@PrimaryKeyJoinColumn(name = "id")
public class UserEntity extends RequisitanteEntity {

    @Column(nullable = false, unique = true)
    @Pattern(regexp = "\\S+", message = "O nome de usuário não pode conter espaços em branco")
    private String username;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", referencedColumnName = "id")
    @JsonBackReference
    private TeamEntity teamEntity;

    @Length(min = 8, max = 100, message = "A senha deve ter pelo menos 8 caracteres")
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role = Role.ADMIN;

    private boolean firstAccess = true;
}