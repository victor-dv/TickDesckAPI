package br.com.tick.tickdesck.models.user.infra;

import br.com.tick.tickdesck.models.user.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email);
    Optional<UserEntity> findByEmailOrUsername(String email, String username);
    List<UserEntity> findByTeamEntityId(Long teamId);
    @Query("SELECT u FROM UserEntity u WHERE u.teamEntity.enterprise.id = :enterpriseId")
    List<UserEntity> findByEnterpriseId(@Param("enterpriseId") Long enterpriseId);
}
