package br.com.tick.tickdesck.infraestructure.user;

import br.com.tick.tickdesck.domain.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
}
