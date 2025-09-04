package br.com.tick.tickdesck.models.call.application;

import br.com.tick.tickdesck.models.call.application.dto.CreateCallDto;
import br.com.tick.tickdesck.models.call.application.dto.UpdateCallDto;
import br.com.tick.tickdesck.models.call.domain.CallsEntity;
import br.com.tick.tickdesck.models.call.infra.CallRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CallService {

    @Autowired
    private CallRepository callRepository;

    public CallsEntity createCall(CreateCallDto callRequestDto) {

        if (callRequestDto.emailEnvio().isEmpty() || callRequestDto.usernameEnvio().isEmpty()|| callRequestDto.urgencia() == 0) {
            throw new IllegalArgumentException("Campos obrigatórios.");
        }

        CallsEntity callEntity = new CallsEntity();
        callEntity.setEmailEnvio(callRequestDto.emailEnvio());
        callEntity.setUsernameEnvio(callRequestDto.usernameEnvio());
        callEntity.setUrgencia(callRequestDto.urgencia());

        return callRepository.save(callEntity);
    }

    public CallsEntity callsList(int callNumber) {
        return callRepository.findByCallNumber(callNumber)
                .orElseThrow(() -> new IllegalArgumentException("Chamado não encontrado"));
    }

    public CallsEntity updateCall(int callNumber, UpdateCallDto updatedCallDto) {
        CallsEntity existingCall = callRepository.findByCallNumber(callNumber)
                .orElseThrow(() -> new IllegalArgumentException("Chamado não encontrado"));

        existingCall.setPrevisaoSolucao(updatedCallDto.previsaoSolucao());
        existingCall.setDataFechamento(updatedCallDto.dataFechamento());

        return callRepository.save(existingCall);
    }
}
