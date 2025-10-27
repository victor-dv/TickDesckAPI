package br.com.tick.tickdesck.models.user_externo.infra;

import br.com.tick.tickdesck.models.user_externo.domain.UserExternoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserExternoRepository extends JpaRepository<UserExternoEntity, Long> {

}
