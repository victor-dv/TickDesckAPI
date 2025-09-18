package br.com.tick.tickdesck.models.call.application;

import br.com.tick.tickdesck.models.call.application.actiondto.ActionDto;
import br.com.tick.tickdesck.models.call.domain.Actions;
import br.com.tick.tickdesck.models.call.infra.ActionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ActionService {

    @Autowired
    private ActionRepository actionRepository;

    public Actions createAction(ActionDto actionDto) {

        Actions action = new Actions();
        action.setDescription(actionDto.description());
        action.setPublica(actionDto.publica());
        action.setDataCadastro(LocalDateTime.now());

        return actionRepository.save(action);
    }

    public Actions getAction(Long id) {
        var action = actionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Ação não encontrada"));
        return action;
    }

    public Actions updateAction(Long id, Actions updatedAction) {
        Actions existingAction = actionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Ação não encontrada"));

        existingAction.setDescription(updatedAction.getDescription());
        existingAction.setPublica(updatedAction.getPublica());
        existingAction.setDataCadastro(LocalDateTime.now());

        return actionRepository.save(existingAction);
    }

    public Actions deleteAction(Long id) {
        Actions existingAction = actionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Ação não encontrada"));
        actionRepository.delete(existingAction);
        return existingAction;
    }
}


