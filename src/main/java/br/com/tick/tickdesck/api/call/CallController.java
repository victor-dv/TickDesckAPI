package br.com.tick.tickdesck.api.call;

import br.com.tick.tickdesck.models.call.application.CallService;
import br.com.tick.tickdesck.models.call.application.dto.ClientUpdateCallDto;
import br.com.tick.tickdesck.models.call.application.dto.CreateCallDto;
import br.com.tick.tickdesck.models.call.application.dto.UpdateCallDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/calls")
public class CallController {

    @Autowired
    private CallService callService;

    // rota para criar um chamado
    @PostMapping("/")
    public ResponseEntity<?> createCall(@Valid @RequestBody CreateCallDto callRequestDto) {

            var result = this.callService.createCall(callRequestDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(result);

    }

    //Rota para listar um chamado pelo número do chamado
    @GetMapping("/{callNumber}")
    public ResponseEntity<?> getCall(@PathVariable int callNumber) {

            var result = this.callService.getCall(callNumber);
            return ResponseEntity.status(HttpStatus.OK).body(result);

    }

    @GetMapping("/business/{idEmpresa}")
    public ResponseEntity<?> getCallByBusiness(@PathVariable Long idEmpresa) {

            var result = this.callService.callByBusiness(idEmpresa);
            return ResponseEntity.status(HttpStatus.OK).body(result);

    }

    @GetMapping("/team/{idEquipe}")
    public ResponseEntity<?> getCallByTeam(@PathVariable Long idEquipe) {

            var result = this.callService.callByTeam(idEquipe);
            return ResponseEntity.status(HttpStatus.OK).body(result);

    }

    @GetMapping("/client/{id}")
    public ResponseEntity<?> getCallByClient(@PathVariable Long id) {

            var result = this.callService.callByClient(id);
            return ResponseEntity.status(HttpStatus.OK).body(result);

    }

    //  Rota para listar todos os chamados
    @GetMapping("/list")
    public ResponseEntity<?> listCalls() {

            var result = this.callService.ListCall();
            return ResponseEntity.status(HttpStatus.OK).body(result);

    }

    //Rota para atualizar um chamado pelo número do chamado
    @PutMapping("/{callNumber}")
    public ResponseEntity<?> updateCall(@Valid @PathVariable int callNumber, @RequestBody UpdateCallDto updatedCallDto) {

            var result = this.callService.updateCall(callNumber, updatedCallDto);
            return ResponseEntity.status(HttpStatus.OK).body(result);

    }

    //Rota para deletar um chamado pelo número do chamado
    @DeleteMapping("/{callNumber}")
    public ResponseEntity<?> deleteCall(@PathVariable int callNumber) {

            var result = this.callService.deleteCall(callNumber);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(result);

    }
}
