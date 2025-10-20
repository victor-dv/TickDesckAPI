package br.com.tick.tickdesck.models.auditoria_call.application.controller;

import br.com.tick.tickdesck.models.auditoria_call.application.ActionService;
import br.com.tick.tickdesck.models.auditoria_call.application.dto.CreateActionDto;
import br.com.tick.tickdesck.models.auditoria_call.application.dto.ResponseActionDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("api/actions")
@RestController
public class ActionController {

    @Autowired
    private ActionService actionService;

    // Criar uma action
    @PostMapping("/")
    public ResponseEntity<ResponseActionDto> create(@RequestBody CreateActionDto createActionDto) {
        var result = this.actionService.createAction(createActionDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseActionDto.fromEntity(result));
    }

    // Buscar uma action por ID
    @GetMapping("/{id}")
    public ResponseEntity<ResponseActionDto> getById(@PathVariable Long id) {
        var result = this.actionService.getActionById(id);
        return ResponseEntity.ok(ResponseActionDto.fromEntity(result));
    }

    // Listar todas as actions de um chamado
    @GetMapping("/call/{callId}")
    public ResponseEntity<List<ResponseActionDto>> listByCall(@PathVariable Long callId) {
        var result = this.actionService.listActionsByCall(callId)
                .stream()
                .map(ResponseActionDto::fromEntity)
                .toList();
        return ResponseEntity.ok(result);
    }

    // Listar todas as actions de um usuário
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ResponseActionDto>> listByUser(@PathVariable Long userId) {
        var result = this.actionService.listActionsByUser(userId)
                .stream()
                .map(ResponseActionDto::fromEntity)
                .toList();
        return ResponseEntity.ok(result);
    }

    // Deletar uma action
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        this.actionService.deleteAction(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}

