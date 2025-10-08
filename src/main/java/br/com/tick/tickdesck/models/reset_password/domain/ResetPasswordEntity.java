package br.com.tick.tickdesck.models.reset_password.domain;

import br.com.tick.tickdesck.models.user.domain.UserEntity;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class ResetPasswordEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String token;
    @OneToOne
    private UserEntity user;
    private LocalDateTime expiryDate;
}
