package br.com.tick.tickdesck.models.call.application;

import br.com.tick.tickdesck.models.call.application.dto.CreateCallDto;
import br.com.tick.tickdesck.models.call.application.dto.UpdateCallDto;
import br.com.tick.tickdesck.models.call.domain.CallsEntity;
import br.com.tick.tickdesck.models.call.infra.CallRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CallService {

    @Autowired
    private CallRepository callRepository;

    // Lógica para criar um chamado
    public CallsEntity createCall(CreateCallDto callRequestDto) {

        CallsEntity callEntity = new CallsEntity();
        callEntity.setEmailEnvio(callRequestDto.emailEnvio());
        callEntity.setUsernameEnvio(callRequestDto.usernameEnvio());
        callEntity.setStatus(callRequestDto.status());
        callEntity.setUrgencia(callRequestDto.urgencia());
        callEntity.setPrevisaoSolucao(callRequestDto.previsaoSolucao());

        return callRepository.save(callEntity);

    }

    //  Lógica para listar um chamado pelo número do chamado
    public CallsEntity getCall(int callNumber) {
        return callRepository.findByCallNumber(callNumber)
                .orElseThrow(() -> new IllegalArgumentException("Chamado não encontrado"));
    }

    //  Lógica para listar todos os chamados
    public ResponseEntity<List<CallsEntity>> ListCall() {
        List<CallsEntity> calls = callRepository.findAll();
        if (calls.isEmpty()) {
            throw new IllegalArgumentException("Nenhum chamado encontrado");
        }
        return ResponseEntity.status(HttpStatus.OK).body(calls);
    }

    // Lógica para atualizar um chamado pelo número do chamado
    public CallsEntity updateCall(int callNumber, UpdateCallDto updatedCallDto) {
        CallsEntity existingCall = callRepository.findByCallNumber(callNumber)
                .orElseThrow(() -> new IllegalArgumentException("Chamado não encontrado"));

        existingCall.setStatus(updatedCallDto.status());
        existingCall.setDataFechamento(updatedCallDto.dataFechamento());
        existingCall.setUsuarioFechamento(updatedCallDto.usuarioFechamento());
        existingCall.setUrgencia(updatedCallDto.urgencia());



        return callRepository.save(existingCall);
    }
    // Lógica para deletar um chamado pelo número do chamado
    public CallsEntity deleteCall(int callNumber) {
        CallsEntity existingCall = callRepository.findByCallNumber(callNumber)
                .orElseThrow(() -> new IllegalArgumentException("Chamado não encontrado"));

        callRepository.delete(existingCall);

        return existingCall;
    }

}
