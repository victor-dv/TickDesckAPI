package br.com.tick.tickdesck.models.password.domain;

import br.com.tick.tickdesck.models.user_interno.domain.UserEntity;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
public class ResetPasswordEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String token;
    @OneToOne
    private UserEntity user;
    private LocalDateTime expirationDate;
}
