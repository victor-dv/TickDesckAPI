package br.com.tick.tickdesck.application;

import br.com.tick.tickdesck.application.dto.CallRequestDto;
import br.com.tick.tickdesck.domain.call.CallsEntity;
import br.com.tick.tickdesck.domain.user.UserEntity;
import br.com.tick.tickdesck.infraestructure.calls.CallRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CallService {

    @Autowired
    private CallRepository callRepository;

    public CallsEntity createCall(CallRequestDto callRequestDto) {

        if (callRequestDto.emailEnvio().isEmpty() || callRequestDto.usernameEnvio().isEmpty()) {
            throw new IllegalArgumentException("Campos obrigatórios.");
        }



        CallsEntity callEntity = new CallsEntity();
        callEntity.setEmailEnvio(callRequestDto.usernameEnvio());
        callEntity.setUsernameEnvio(callRequestDto.usernameEnvio());
        callEntity.setUrgencia(callRequestDto.urgencia());

        return callRepository.save(callEntity);
    }

    public CallsEntity callsList(int callNumber) {
        return callRepository.findByCallNumber(callNumber)
                .orElseThrow(() -> new IllegalArgumentException("Chamado não encontrado"));
    }

    public CallsEntity updateCall(int callNumber, CallRequestDto updatedCallDto) {
        CallsEntity existingCall = callRepository.findByCallNumber(callNumber)
                .orElseThrow(() -> new IllegalArgumentException("Chamado não encontrado"));

        existingCall.setEmailEnvio(updatedCallDto.emailEnvio());
        existingCall.setUsernameEnvio(updatedCallDto.usernameEnvio());
        existingCall.setUrgencia(updatedCallDto.urgencia());
        existingCall.setPrevisaoSolucao(updatedCallDto.previsaoSolucao());

        // Update other fields as needed

        return callRepository.save(existingCall);
    }
}
