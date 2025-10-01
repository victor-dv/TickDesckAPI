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
    public ResponseEntity<?> list() {
        var result = this.callService.listCall().stream().map(ResponseCallDto::fromCallEntity).toList();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseCallDto> update(@PathVariable Long id, @RequestBody UpdateCallDto dto) {
        var result = ResponseCallDto.fromCallEntity(this.callService.updateCall(id, dto));
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CallsEntity> delete(@PathVariable Long id) {
       this.callService.deleteCall(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
