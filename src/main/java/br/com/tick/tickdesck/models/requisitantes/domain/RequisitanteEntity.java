package br.com.tick.tickdesck.models.requisitantes.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter // SUBSTITUA
@Setter // SUBSTITUA
@NoArgsConstructor
@Entity
@Table(name = "requisitantes")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class RequisitanteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false) // Boa prática
    private String name;

    @Email(message = "O email deve ser válido")
    @Column(nullable = false) // Boa prática
    private String email;
}
