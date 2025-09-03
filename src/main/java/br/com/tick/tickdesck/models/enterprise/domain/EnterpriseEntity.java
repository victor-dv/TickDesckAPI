package br.com.tick.tickdesck.models.enterprise.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
@Entity
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
}
