package br.com.tick.tickdesck.models.password.repository;

import br.com.tick.tickdesck.models.password.domain.ResetPasswordEntity;
import br.com.tick.tickdesck.models.user_interno.domain.UserEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ResetPasswordRepository extends JpaRepository<ResetPasswordEntity, UUID> {

    Optional<ResetPasswordEntity> findByToken(String token);
    @Transactional
    @Modifying
    @Query("DELETE FROM ResetPasswordEntity t WHERE t.user = :user")
    void deleteByUser(@Param("user") UserEntity user);
}
