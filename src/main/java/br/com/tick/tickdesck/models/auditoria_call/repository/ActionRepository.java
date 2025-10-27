package br.com.tick.tickdesck.models.auditoria_call.repository;

import br.com.tick.tickdesck.models.auditoria_call.application.dto.RoleStatusAction;
import br.com.tick.tickdesck.models.auditoria_call.domain.ActionEntity;
import br.com.tick.tickdesck.models.call.domain.CallsEntity;
import br.com.tick.tickdesck.models.user_interno.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActionRepository extends JpaRepository<ActionEntity, Long> {

    List<ActionEntity> findByCallsEntity(CallsEntity call);
    List<ActionEntity> findByUser(UserEntity user);
    List<ActionEntity> findByStatusAction(RoleStatusAction statusAction);
}
