package br.com.tick.tickdesck.models.call.infra;

import br.com.tick.tickdesck.models.call.domain.CallsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CallRepository extends JpaRepository<CallsEntity,Long> {

    Optional<CallsEntity> findByCallNumber(int callNumber);
}
