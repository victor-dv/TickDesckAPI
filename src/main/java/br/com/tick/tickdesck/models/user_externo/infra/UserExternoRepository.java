package br.com.tick.tickdesck.models.user_externo.infra;

import br.com.tick.tickdesck.models.user_externo.domain.UserExternoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserExternoRepository extends JpaRepository<UserExternoEntity, Long> {

}
