package br.com.tick.tickdesck.infraestructure.call.user;

import br.com.tick.tickdesck.domain.call.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
}
