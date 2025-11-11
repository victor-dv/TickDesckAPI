package br.com.tick.tickdesck.models.user_externo.application.controller;

import br.com.tick.tickdesck.models.user_externo.application.UserExternoService;
import br.com.tick.tickdesck.models.user_externo.application.dto.ResponseUserExternoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/user-externo")
public class UserExternoController {

    @Autowired
    public UserExternoService userExternoService;

    @GetMapping("/{id}")
    public ResponseEntity<ResponseUserExternoDto> getUserExternoById(@PathVariable Long id) {
        var result = ResponseUserExternoDto.fromUserExternoEntity(userExternoService.getUserById(id));
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/")
    public ResponseEntity<List<ResponseUserExternoDto>> getAllUserExternos() {
        var result = userExternoService.getAll()
                .stream()
                .map(ResponseUserExternoDto::fromUserExternoEntity)
                .toList();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
