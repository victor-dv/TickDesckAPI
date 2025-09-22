package br.com.tick.tickdesck.models.user.infra;

import br.com.tick.tickdesck.models.user.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email);
    Optional<UserEntity> findByEmailOrUsername(String email, String username);
    boolean existsByTeamId(Long teamId);
}
