package br.com.tick.tickdesck.models.call.application.controller;

import br.com.tick.tickdesck.models.call.application.CallService;
import br.com.tick.tickdesck.models.call.application.dto.CreateCallDto;
import br.com.tick.tickdesck.models.call.application.dto.ResponseCallDto;
import br.com.tick.tickdesck.models.call.application.dto.UpdateCallDto;
import br.com.tick.tickdesck.models.call.domain.CallsEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/calls")
public class CallController {

    @Autowired
    private CallService callService;

    @PostMapping
    public ResponseEntity<ResponseCallDto> create(@RequestBody CreateCallDto dto) {
        var result = ResponseCallDto.fromCallEntity(this.callService.createCall(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseCallDto> getById(@PathVariable Long id) {
        var result = ResponseCallDto.fromCallEntity(this.callService.getCall(id));
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/")
    public ResponseEntity<List<ResponseCallDto>> list() {
        var result = this.callService.listCall()
                .stream()
                .map(ResponseCallDto::fromCallEntity)
                .toList();
        return ResponseEntity.ok(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseCallDto> update(@PathVariable Long id, @RequestBody UpdateCallDto dto) {
        var result = ResponseCallDto.fromCallEntity(this.callService.updateCall(id, dto));
        return ResponseEntity.ok(result);
    }

    @GetMapping("/team/{idEquipe}")
    public ResponseEntity<List<ResponseCallDto>> getCallByTeam(@PathVariable Long idEquipe) {
        var result = this.callService.callByTeam(idEquipe)
                .stream()
                .map(ResponseCallDto::fromCallEntity)
                .toList();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/enterprise/{idEmpresa}")
    public ResponseEntity<List<ResponseCallDto>> getCallByBusiness(@PathVariable Long idEmpresa) {
        var result = this.callService.callByEnterprise(idEmpresa)
                .stream()
                .map(ResponseCallDto::fromCallEntity)
                .toList();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/user/{idUser}")
    public ResponseEntity<List<ResponseCallDto>> getCallByUser(@PathVariable Long idUser) {
        var result = this.callService.userResponsavel(idUser)
                .stream()
                .map(ResponseCallDto::fromCallEntity)
                .toList();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/search/")
    public ResponseEntity<List<ResponseCallDto>> searchCalls(@RequestParam String query) {
        List<ResponseCallDto> result;

        try {
            Integer numberCall = Integer.parseInt(query);
            result = this.callService.buscarPorNumero(numberCall)
                    .stream()
                    .map(ResponseCallDto::fromCallEntity)
                    .toList();
        } catch (NumberFormatException e) {
            // Se não for número, busca por título
            result = this.callService.buscarPorTitulo(query)
                    .stream()
                    .map(ResponseCallDto::fromCallEntity)
                    .toList();
        }

        return ResponseEntity.ok(result);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        this.callService.deleteCall(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/reopen/{id}")
    public ResponseEntity<ResponseCallDto> reopenCall(@PathVariable Long id) {
        var result = ResponseCallDto.fromCallEntity(this.callService.reOpenCall(id));
        return ResponseEntity.ok(result);
    }
}
