package br.com.tick.tickdesck.domain.user;


import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.UUID;

@Entity
@Data
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String name;
    @Column(nullable = false, unique = true)
    @Pattern(regexp = "\\S+", message = "O nome de usuário não pode conter espaços em branco")
    private String username;
    @Email(message = "O email deve ser válido")
    private String email;

    @Length(min = 8,max = 50, message = "A senha deve ter pelo menos 8 caracteres")
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private String role = "USER"; //
}
