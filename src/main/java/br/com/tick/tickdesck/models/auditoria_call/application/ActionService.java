    package br.com.tick.tickdesck.models.auditoria_call.application;

    import br.com.tick.tickdesck.models.auditoria_call.application.dto.CreateActionDto;
    import br.com.tick.tickdesck.models.auditoria_call.domain.ActionEntity;
    import br.com.tick.tickdesck.models.auditoria_call.repository.ActionRepository;
    import br.com.tick.tickdesck.models.call.infra.CallRepository;
    import br.com.tick.tickdesck.models.user.infra.UserRepository;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Service;

    import java.util.List;

    @Service
    public class ActionService {

        @Autowired
        private ActionRepository actionRepository;

        @Autowired
        private UserRepository userRepository;

        @Autowired
        private CallRepository callRepository;

        // Criar uma Action
        public ActionEntity createAction(CreateActionDto createActionDto) {
            var user = userRepository.findById(createActionDto.userId())
                    .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));
            var call = callRepository.findById(createActionDto.callId())
                    .orElseThrow(() -> new IllegalArgumentException("Chamado não encontrado"));

            ActionEntity action = new ActionEntity();
            action.setDescription(createActionDto.description());
            action.setUser(user);
            action.setCallsEntity(call);

            return actionRepository.save(action);
        }

        // Buscar action por ID
        public ActionEntity getActionById(Long id) {
            return actionRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Action não encontrada"));
        }

        // Listar todas as actions de um chamado
        public List<ActionEntity> listActionsByCall(Long callId) {
            var call = callRepository.findById(callId)
                    .orElseThrow(() -> new IllegalArgumentException("Chamado não encontrado"));
            return actionRepository.findByCallsEntity(call);
        }

        // Listar todas as actions de um usuário
        public List<ActionEntity> listActionsByUser(Long userId) {
            var user = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));
            return actionRepository.findByUser(user);
        }

        // Deletar uma action (caso necessário)
        public void deleteAction(Long id) {
            var action = actionRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Action não encontrada"));
            actionRepository.delete(action);
        }
    }

