package br.com.tick.tickdesck.api.user;

import br.com.tick.tickdesck.application.user.UserService;
import br.com.tick.tickdesck.domain.user.UserEntity;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/user")
public class UserController {

    @Autowired
    private UserService userService;

    //Metodo para criar um usuário
    //Ele recebe um objeto UserEntity e retorna o usuário criado
    //Se ocorrer algum erro, retorna uma mensagem de erro
    //Se o usuário já existir, lança uma exceção e retorna uma mensagem de erro
    //Se o usuário for criado com sucesso, retorna o usuário criado
    @PostMapping("/")
    public ResponseEntity<?> createUser(@Valid @RequestBody UserEntity userEntity) {
        try {
            UserEntity createdUser = userService.createUser(userEntity);
            return ResponseEntity.ok(createdUser);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Ocorreu um erro interno no servidor" + e.getMessage());
        }
    }


}
