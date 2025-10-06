package br.com.tick.tickdesck.models.call.infra;

import br.com.tick.tickdesck.models.call.domain.CallsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface CallRepository extends JpaRepository<CallsEntity, Long> {

    Optional<CallsEntity> findById(Long id);

    List<CallsEntity> findByStatusTrue();

    List<CallsEntity> findByUserExternoIdAndStatusTrue(Long userExternoId);

    List<CallsEntity> findByTeamIdAndStatusTrue(Long teamId);
    List<CallsEntity> findByTeam_Enterprise_Id(Long enterpriseId);

    List<CallsEntity> findByUserResponsavelIdAndStatusTrue(Long userId);
}
