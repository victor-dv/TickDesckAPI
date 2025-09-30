package br.com.tick.tickdesck.api.call;

import br.com.tick.tickdesck.models.call.application.CallService;
import br.com.tick.tickdesck.models.call.application.dto.CreateCallDto;
import br.com.tick.tickdesck.models.call.application.dto.UpdateCallDto;
import br.com.tick.tickdesck.models.call.domain.CallsEntity;
import jakarta.validation.Valid;
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
    public ResponseEntity<CallsEntity> create(@RequestBody CreateCallDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(callService.createCall(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CallsEntity> getById(@PathVariable Long id) {
        return ResponseEntity.ok(callService.getCall(id));
    }

    @GetMapping
    public ResponseEntity<List<CallsEntity>> list() {
        return ResponseEntity.ok(callService.listCall());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CallsEntity> update(@PathVariable Long id, @RequestBody UpdateCallDto dto) {
        return ResponseEntity.ok(callService.updateCall(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CallsEntity> delete(@PathVariable Long id) {
        return ResponseEntity.ok(callService.deleteCall(id));
    }
}
