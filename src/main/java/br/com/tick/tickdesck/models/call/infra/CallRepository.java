package br.com.tick.tickdesck.models.call.infra;

import br.com.tick.tickdesck.models.call.application.dto.UrgenciaCallDto;
import br.com.tick.tickdesck.models.call.domain.CallsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface CallRepository extends JpaRepository<CallsEntity, Long> {

    Optional<CallsEntity> findById(Long id);

    List<CallsEntity> findByTeamIdAndStatusTrue(Long teamId);

    List<CallsEntity> findByTeam_Enterprise_Id(Long enterpriseId);

    List<CallsEntity> findByUserResponsavelIdAndStatusTrue(Long userId);

    @Query("SELECT COALESCE(MAX(c.numberCall), 0) FROM CallsEntity c WHERE c.team.enterprise.id = :enterpriseId")
    Integer findLastNumeroByEmpresa(@Param("enterpriseId") Long enterpriseId);

    List<CallsEntity> findByTitleContainingIgnoreCase(String title);

    List<CallsEntity> findByNumberCall(Integer numberCall);

    Long countByTeamId(Long teamId);

    Long countByUrgencia(UrgenciaCallDto urgencia);
}
