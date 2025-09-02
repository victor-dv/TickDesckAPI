package br.com.tick.tickdesck.models.enterprise.infra;


import br.com.tick.tickdesck.models.enterprise.domain.EnterpriseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnterpriseRepository extends JpaRepository<EnterpriseEntity, Long> {
}
