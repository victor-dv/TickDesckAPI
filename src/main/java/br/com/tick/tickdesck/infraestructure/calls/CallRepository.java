package br.com.tick.tickdesck.infraestructure.call.calls;

import br.com.tick.tickdesck.domain.call.call.CallsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CallRepository extends JpaRepository<CallsEntity,Long> {


    public CallsEntity findByEmailAndUserName(String EmailEnvio, String UsernameEnvio );

}
