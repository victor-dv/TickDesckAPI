package br.com.tick.tickdesck.infraestructure.calls;

import br.com.tick.tickdesck.domain.call.CallsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CallRepository extends JpaRepository<CallsEntity,Long> {

    Optional<CallsEntity> findByCallNumber(int callNumber);

}
