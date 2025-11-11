package br.com.tick.tickdesck.models.requisitantes.application.controller;

import br.com.tick.tickdesck.models.requisitantes.application.RequisitanteService;
import br.com.tick.tickdesck.models.requisitantes.application.dto.ResponseRequisitanteDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/requisitante")
public class RequisitanteController {

    @Autowired
    private RequisitanteService requisitanteService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getRequisitanteById(@PathVariable Long id) {
        var requisitante = ResponseRequisitanteDto.fromUserExternoEntity(requisitanteService.getRequisitanteById(id));
        return ResponseEntity.ok(requisitante);
    }
}
