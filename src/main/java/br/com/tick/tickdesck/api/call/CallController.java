package br.com.tick.tickdesck.api.call;

import br.com.tick.tickdesck.models.call.application.CallService;
import br.com.tick.tickdesck.models.call.application.dto.CreateCallDto;
import br.com.tick.tickdesck.models.call.application.dto.UpdateCallDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/calls")
public class CallController {

    @Autowired
    private CallService callService;

    @PostMapping("/")
    public ResponseEntity<?> createCall(@Valid @RequestBody CreateCallDto callRequestDto) {
        try {
            var result = this.callService.createCall(callRequestDto);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ocorreu um erro interno no servidor");
        }
    }

    @GetMapping("/{callNumber}")
    public ResponseEntity<?> callsList(@PathVariable int callNumber) {
        try {
            var result = this.callService.callsList(callNumber);

          return ResponseEntity.ok(result);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Chamado não encontrado");
        }
        catch (ExceptionInInitializerError e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ocorreu um erro interno no servidor");
        }
    }
    @PutMapping("/{callNumber}")
    public ResponseEntity<?> updateCall(@PathVariable int callNumber, @RequestBody UpdateCallDto updatedCallDto) {
        try {
            var result = this.callService.updateCall(callNumber, updatedCallDto);
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ocorreu um erro interno no servidor");
        }
    }
}
