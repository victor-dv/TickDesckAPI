package br.com.tick.tickdesck.models.call.infra;

import br.com.tick.tickdesck.models.call.domain.Actions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ActionRepository extends JpaRepository<Actions,Long> {

    Optional<Actions> findById(Long id);
}
