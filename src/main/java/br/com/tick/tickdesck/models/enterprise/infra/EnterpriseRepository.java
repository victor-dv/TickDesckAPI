package br.com.tick.tickdesck.models.enterprise.infra;


import br.com.tick.tickdesck.models.enterprise.domain.EnterpriseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EnterpriseRepository extends JpaRepository<EnterpriseEntity, Long> {

    Optional<EnterpriseEntity> findByCnpjOrEmail(String cnpj, String email);
    Optional<EnterpriseEntity> findByEmailOrCnpjOrPhone(String email, String cnpj, String phone);
}
