package br.com.tick.tickdesck.models.user.infra;

import br.com.tick.tickdesck.models.user.domain.UserExternoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserExternoRepository extends JpaRepository<UserExternoEntity, Long> {

}
