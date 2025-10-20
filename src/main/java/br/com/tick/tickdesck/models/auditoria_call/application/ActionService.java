    package br.com.tick.tickdesck.models.auditoria_call.application;

    import br.com.tick.tickdesck.models.auditoria_call.application.dto.CreateActionDto;
    import br.com.tick.tickdesck.models.auditoria_call.domain.ActionEntity;
    import br.com.tick.tickdesck.models.auditoria_call.repository.ActionRepository;
    import br.com.tick.tickdesck.models.call.infra.CallRepository;
    import br.com.tick.tickdesck.models.user.infra.UserRepository;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.security.core.context.SecurityContextHolder;
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

        public ActionEntity createAction(CreateActionDto createActionDto) {
            var authentication = SecurityContextHolder.getContext().getAuthentication();

            var loggedUser = userRepository.findById(Long.decode((String) authentication.getName()))
                    .orElseThrow(() -> new IllegalArgumentException("Usuário autenticado não encontrado"));
            var call = callRepository.findById(createActionDto.callId())
                    .orElseThrow(() -> new IllegalArgumentException("Chamado não encontrado"));
            var statusCall = call.isStatus();
            if (!statusCall) {
                throw new IllegalArgumentException("Não é possível adicionar ações a um chamado fechado.");
            }
            ActionEntity action = new ActionEntity();
            action.setDescription(createActionDto.description());
            action.setUser(loggedUser);
            action.setCallsEntity(call);

            return actionRepository.save(action);
        }

        public ActionEntity getActionById(Long id) {
            return actionRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Action não encontrada"));
        }

        public List<ActionEntity> listActionsByCall(Long callId) {
            var call = callRepository.findById(callId)
                    .orElseThrow(() -> new IllegalArgumentException("Chamado não encontrado"));
            return actionRepository.findByCallsEntity(call);
        }


        public List<ActionEntity> listActionsByUser(Long userId) {
            var user = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));
            return actionRepository.findByUser(user);
        }


        public void deleteAction(Long id) {
            var action = actionRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Action não encontrada"));
            actionRepository.delete(action);
        }
    }

