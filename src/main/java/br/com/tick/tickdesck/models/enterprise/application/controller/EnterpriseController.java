package br.com.tick.tickdesck.models.enterprise.application.controller;

import br.com.tick.tickdesck.models.enterprise.application.EnterpriseService;
import br.com.tick.tickdesck.models.enterprise.application.dto.CreateEnterpriseDto;
import br.com.tick.tickdesck.models.enterprise.application.dto.ResponseEnterpriseDto;
import br.com.tick.tickdesck.models.enterprise.application.dto.UpdateEnterpriseDto;
import br.com.tick.tickdesck.models.team.application.dto.ResponseTeamDto;
import br.com.tick.tickdesck.models.user.application.dto.ResponseUserDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/enterprise")
public class EnterpriseController {

    @Autowired
    private EnterpriseService enterpriseService;

    @PostMapping("/create")
    public ResponseEntity<?> createEnterprise(@Valid @RequestBody CreateEnterpriseDto createEnterpriseDto) {
        var result = ResponseEnterpriseDto.fromEnterpriseEntity(this.enterpriseService.create(createEnterpriseDto));
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateEnterprise(@PathVariable Long id, @RequestBody UpdateEnterpriseDto updateEnterpriseDto) {
        var result = ResponseEnterpriseDto.fromEnterpriseEntity(this.enterpriseService.update(id, updateEnterpriseDto));
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteEnterprise(@PathVariable Long id) {
        var result = this.enterpriseService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/get")
    public ResponseEntity<?> getAllEnterprises() {
        var result = this.enterpriseService.getAll().stream().map(ResponseEnterpriseDto::fromEnterpriseEntity).toList();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/{id}/teams")
    public ResponseEntity<?> getEnterpriseTeams(@PathVariable Long id) {
        var result = this.enterpriseService.getTeamsByEnterpriseId(id)
                .stream()
                .map(ResponseTeamDto::fromTeamEntity)
                .toList();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
    @GetMapping("/{id}/users")
    public ResponseEntity<?> getEnterpriseUsers(@PathVariable Long id) {
        var result = this.enterpriseService.getUsersByEnterpriseId(id)
                .stream()
                .map(ResponseUserDto::fromUser)
                .toList();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

}
