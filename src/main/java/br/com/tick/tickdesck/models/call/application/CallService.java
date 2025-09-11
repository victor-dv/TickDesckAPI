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

    // Cria um novo chamado a partir dos dados recebidos
    public CallsEntity createCall(CreateCallDto callRequestDto) {


        CallsEntity callEntity = new CallsEntity();
        callEntity.setIdCliente(callRequestDto.idCliente());
        callEntity.setEmailEnvio(callRequestDto.emailEnvio());
        callEntity.setIdEquipe(callRequestDto.idEquipe());
        callEntity.setUsernameEnvio(callRequestDto.usernameEnvio());
        callEntity.setStatus(callRequestDto.status());
        callEntity.setUrgencia(callRequestDto.urgencia());
        callEntity.setPrevisaoSolucao(callRequestDto.previsaoSolucao());

        // Salva o chamado no banco de dados
        return callRepository.save(callEntity);

    }

    // Busca um chamado pelo número, lança exceção se não encontrar
    public CallsEntity getCall(int callNumber) {
        var call = callRepository.findByCallNumber(callNumber)
                .orElseThrow(() -> new IllegalArgumentException("Chamado não encontrado"));
        if (!call.isStatus()) {
            throw new IllegalArgumentException("Chamado está fechado");
        }
        return call;
    }

    // Lista todos os chamados, lança exceção se não houver nenhum
    public ResponseEntity<List<CallsEntity>> ListCall() {
        List<CallsEntity> calls = callRepository.findByStatusTrue();
        if (calls.isEmpty()) {
            throw new IllegalArgumentException("Nenhum chamado encontrado");
        }
        return ResponseEntity.status(HttpStatus.OK).body(calls);
    }

    //Lista chamados por equipe
    public List<CallsEntity> callByTeam(Long idEquipe) {

        List<CallsEntity> calls = callRepository.findByIdEquipeAndStatusTrue(idEquipe);
        if (calls.isEmpty()) {
            throw new IllegalArgumentException("Nenhum chamado encontrado para a equipe");
        }
        return calls;
    }

    public List<CallsEntity> callByClient(Long id) {
        List<CallsEntity> calls = callRepository.findByIdClienteAndStatusTrue(id);
        if (calls.isEmpty()) {
            throw new IllegalArgumentException("Nenhum chamado encontrado para o cliente");
        }
        return calls;
    }

    // Atualiza os dados de um chamado existente
    public CallsEntity updateCall(int callNumber, UpdateCallDto updatedCallDto) {
        CallsEntity existingCall = callRepository.findByCallNumber(callNumber)
                .orElseThrow(() -> new IllegalArgumentException("Chamado não encontrado"));

        existingCall.setDataFechamento(updatedCallDto.dataFechamento());
        existingCall.setUsuarioFechamento(updatedCallDto.usuarioFechamento());
        existingCall.setUrgencia(updatedCallDto.urgencia());

        return callRepository.save(existingCall);
    }

    // Remove um chamado pelo número, retorna o chamado removido
    public CallsEntity deleteCall(int callNumber) {
        CallsEntity existingCall = callRepository.findByCallNumber(callNumber)
                .orElseThrow(() -> new IllegalArgumentException("Chamado não encontrado"));


        existingCall.setStatus(!existingCall.isStatus());// Marca o chamado como fechado
        callRepository.save(existingCall);// Salva a alteração no banco de dados
        return existingCall;
    }

}
