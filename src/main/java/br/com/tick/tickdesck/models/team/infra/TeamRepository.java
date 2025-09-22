package br.com.tick.tickdesck.models.team.infra;

import br.com.tick.tickdesck.models.team.domain.TeamEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TeamRepository extends JpaRepository<TeamEntity, Long> {
    Optional<TeamEntity> findByName(String name);
}
