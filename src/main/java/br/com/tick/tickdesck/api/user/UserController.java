package br.com.tick.tickdesck.api.user;

import br.com.tick.tickdesck.application.dto.AuthUserRequestDto;
import br.com.tick.tickdesck.application.user.AuthUserService;
import br.com.tick.tickdesck.application.user.UserService;
import br.com.tick.tickdesck.domain.user.UserEntity;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    @Autowired
    private AuthUserService authUserService;

    /*Metodo para criar um usuário
     *Ele recebe um objeto UserEntity e retorna o usuário criado
     *Se ocorrer algum erro, retorna uma mensagem de erro
     *Se o usuário já existir, lança uma exceção e retorna uma mensagem de erro
     *Se o usuário for criado com sucesso, retorna o usuário criado */
    @PostMapping("/register")
    public ResponseEntity<?> createUser(@Valid @RequestBody UserEntity userEntity) {
        try {
            var result = this.userService.createUser(userEntity);
            return ResponseEntity.status(HttpStatus.CREATED).body("Usuário criado com sucesso: " + result);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Ocorreu um erro interno no servidor");
        }
    }

    /*
     * Metodo para autenticar um usuário
     * Ele recebe um objeto AuthUserRequestDto e retorna um token JWT
     * Se a autenticação for bem-sucedida, retorna o token com status 200
     * Se a autenticação falhar, retorna uma mensagem de erro com status 401
     * Se ocorrer algum erro interno no servidor, retorna uma mensagem de erro com status 500
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthUserRequestDto authUserRequestDto) {
        try {
            var token = this.authUserService.execute(authUserRequestDto);
            return ResponseEntity.status(HttpStatus.OK).body(token);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Falha na autenticação: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ocorreu um erro interno no servidor: " + e.getMessage());
        }
    }



}
