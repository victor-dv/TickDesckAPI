package br.com.tick.tickdesck.models.requisitantes.repository;

import br.com.tick.tickdesck.models.requisitantes.domain.RequisitanteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RequisitanteRepository extends JpaRepository<RequisitanteEntity, Long> {
    Optional<RequisitanteEntity> findByEmailIgnoreCase(String email);

}
