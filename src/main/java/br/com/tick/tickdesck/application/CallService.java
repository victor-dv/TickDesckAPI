package br.com.tick.tickdesck.application.call;

import br.com.tick.tickdesck.domain.call.call.CallsEntity;
import br.com.tick.tickdesck.infraestructure.call.calls.CallRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CallService {

    @Autowired
    private CallRepository callRepository;

    public CallService createCall(CallsEntity callsEntity) {
        this.callRepository
                .findByEmailAndUserName
                        (callsEntity.getEmailEnvio(),
                            callsEntity.getUsernameEnvio());


        return this.callRepository.save(callsEntity);
    }
}
