package br.com.tick.tickdesck.api.call;

import br.com.tick.tickdesck.models.call.application.ActionService;
import br.com.tick.tickdesck.models.call.application.actiondto.ActionDto;
import br.com.tick.tickdesck.models.call.domain.Actions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/calls/actions")
public class ActionController {

    @Autowired
    private ActionService actionService;

    @PostMapping("/")
    public ResponseEntity<?> createAction(@RequestBody ActionDto actionDto) {
        try {
            var result = this.actionService.createAction(actionDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAction(@PathVariable Long id) {
        try {
            var result = this.actionService.getAction(id);
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno do servidor");
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateAction(@PathVariable Long id, @RequestBody Actions updatedAction) {
        try {
            var result = this.actionService.updateAction(id, updatedAction);
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno do servidor");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAction(@PathVariable Long id) {
        try {
            var result = this.actionService.deleteAction(id);
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno do servidor");
        }
    }
}
