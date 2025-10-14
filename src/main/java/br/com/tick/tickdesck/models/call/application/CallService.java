package br.com.tick.tickdesck.models.call.application;

import br.com.tick.tickdesck.models.call.application.dto.CreateCallDto;
import br.com.tick.tickdesck.models.call.application.dto.UpdateCallDto;
import br.com.tick.tickdesck.models.call.application.dto.UrgenciaCallDto;
import br.com.tick.tickdesck.models.call.domain.CallsEntity;
import br.com.tick.tickdesck.models.call.infra.CallRepository;
import br.com.tick.tickdesck.models.team.infra.TeamRepository;
import br.com.tick.tickdesck.models.user.application.dto.Role;
import br.com.tick.tickdesck.models.user.infra.UserExternoRepository;
import br.com.tick.tickdesck.models.user.infra.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class CallService {

    @Autowired
    private CallRepository callRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserExternoRepository userExternoRepository;

    @Autowired
    private TeamRepository teamRepository;

    // Cria um novo chamado a partir dos dados recebidos
    public CallsEntity createCall(CreateCallDto createCallDto) {
/*        var userExterno = userExternoRepository.findById(createCallDto.userExterId())
                .orElseThrow(() -> new IllegalArgumentException("Usuário externo não encontrado"));*/

        var userResponsavel = userRepository.findById(createCallDto.userResponsavelId())
                .orElseThrow(() -> new IllegalArgumentException("Usuário responsável não encontrado"));

        var team = teamRepository.findById(createCallDto.teamId())
                .orElseThrow(() -> new IllegalArgumentException("Equipe não encontrada"));

        var userExterno = userExternoRepository.findById(createCallDto.userExternoId())
                .orElseThrow(() -> new IllegalArgumentException("Usuário externo não encontrado"));

        Integer ultimoNumero = callRepository.findLastNumeroByEmpresa(team.getEnterprise().getId());
        int novoNumero = ultimoNumero + 1;

        CallsEntity call = new CallsEntity();
        call.setNumberCall(novoNumero);
        call.setTitle(createCallDto.title());
        call.setUserExterno(userExterno);
        call.setUserResponsavel(userResponsavel);
        call.setTeam(team);
        call.setStatus(createCallDto.status());
        call.setUrgencia(createCallDto.urgency());
        call.setPrevisaoSolucao(calcularPrevisao(createCallDto.urgency()));
        call.setDataAbertura(createCallDto.dataAbertura());

        return callRepository.save(call);
    }


    // Busca um chamado pelo número, lança exceção se não encontrar
    public CallsEntity getCall(Long id) {
        CallsEntity call = callRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Chamado não encontrado"));
        if (!call.isStatus()) {
            throw new IllegalArgumentException("Chamado está fechado");
        }
        return call;
    }

    // Lista todos os chamados, lança exceção se não houver nenhum
    public List<CallsEntity> listCall() {
        List<CallsEntity> calls = callRepository.findAll();
        if (calls.isEmpty()) {
            throw new IllegalArgumentException("Nenhum chamado encontrado");
        }
        return calls;
    }


    // Atualiza os dados de um chamado existente
    public CallsEntity updateCall(Long id, UpdateCallDto updatedCallDto) {
        CallsEntity existingCall = callRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Chamado não encontrado"));

        if (!existingCall.isStatus()) {
            throw new IllegalArgumentException("Não é possível atualizar um chamado fechado.");
        }


        if (updatedCallDto.title() != null) {
            existingCall.setTitle(updatedCallDto.title());
        }
        if (updatedCallDto.urgency() != null) {
            existingCall.setUrgencia(updatedCallDto.urgency());
        }

      /*  if (updatedCallDto.usuarioFechamento() != null) {
            existingCall.setUserResponsavel(updatedCallDto.usuarioFechamento());
        }*/
/*
        existingCall.setDataFechamento(updatedCallDto.dataFechamento());
*/

        return callRepository.save(existingCall);
    }

    // Remove um chamado pelo número, retorna o chamado removido
    public CallsEntity deleteCall(Long id) {
        CallsEntity existingCall = callRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Chamado não encontrado"));

        existingCall.setStatus(false); // Fecha o chamado
        return callRepository.save(existingCall);
    }

    // Reabre um chamado fechado
    public CallsEntity reOpenCall(Long id) {

        var authentication = SecurityContextHolder.getContext().getAuthentication();
        //Buscando o usuário autenticado no banco de dados
        var loggedUser = userRepository.findById(Long.decode((String) authentication.getName()))
                .orElseThrow(() -> new RuntimeException("Usuário autenticado não encontrado"));
        //Verificando se o usuário tem papel ADMIN ou GERENTE
        if (!loggedUser.getRole().equals(Role.ADMIN) && !loggedUser.getRole().equals(Role.GERENT) && !loggedUser.getRole().equals(Role.SUPORT)) {
            throw new RuntimeException("Apenas usuários com papel ADMIN , GERENT e SUPORT podem reabrir um chamado");
        }

        CallsEntity existingCall = callRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Chamado não encontrado"));

        if (existingCall.isStatus()) {
            throw new IllegalArgumentException("O chamado já está aberto.");
        }

        existingCall.setStatus(true);
        existingCall.setDataAbertura(LocalDateTime.now());
        existingCall.setPrevisaoSolucao(calcularPrevisao(existingCall.getUrgencia()));

        return callRepository.save(existingCall);
    }

    public List<CallsEntity> callByTeam(Long idEquipe) {

        List<CallsEntity> calls = callRepository.findByTeamIdAndStatusTrue(idEquipe);
        if (calls.isEmpty()) {
            throw new IllegalArgumentException("Nenhum chamado encontrado para a equipe");
        }
        return calls;
    }


    public List<CallsEntity> callByEnterprise(Long idEmpresa) {

        List<CallsEntity> calls = callRepository.findByTeam_Enterprise_Id(idEmpresa);
        if (calls.isEmpty()) {
            throw new IllegalArgumentException("Nenhum chamado encontrado para a empresa");
        }
        return calls;
    }

    public List<CallsEntity> userResponsavel(Long idUser) {

        List<CallsEntity> calls = callRepository.findByUserResponsavelIdAndStatusTrue(idUser);
        if (calls.isEmpty()) {
            throw new IllegalArgumentException("Nenhum chamado encontrado para o usuário");
        }
        return calls;
    }

    private LocalDateTime calcularPrevisao(UrgenciaCallDto urgencia) {
        LocalDateTime agora = LocalDateTime.now();

        switch (urgencia) {
            case BAIXA:
                return agora.plusDays(5);
            case ALTA:
                return agora.plusDays(1);
            default:
                return agora.plusDays(3);
        }
    }

    public List<CallsEntity> buscarPorTitulo(String title) {
        return callRepository.findByTitleContainingIgnoreCase(title);
    }

    public List<CallsEntity> buscarPorNumero(Integer numberCall) {
        return callRepository.findByNumberCall(numberCall);
    }


}
