package br.com.tick.tickdesck.models.reset_password.repository;

import br.com.tick.tickdesck.models.user.domain.UserEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ResetPasswordRepository extends JpaRepository<ResetPasswordRepository, Long> {

    Optional<ResetPasswordRepository> findByToken(String token);
    @Transactional
    @Modifying
    @Query("DELETE FROM PasswordResetToken t WHERE t.user = :user")
    void deleteByUser(@Param("user") UserEntity user);
}
