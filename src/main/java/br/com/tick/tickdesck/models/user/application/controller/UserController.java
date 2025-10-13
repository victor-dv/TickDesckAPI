package br.com.tick.tickdesck.models.user.application.controller;

import br.com.tick.tickdesck.models.user.application.AuthUserService;
import br.com.tick.tickdesck.models.user.application.UserService;
import br.com.tick.tickdesck.models.user.application.dto.AuthUserRequestDto;
import br.com.tick.tickdesck.models.user.application.dto.RegisterUserDto;
import br.com.tick.tickdesck.models.user.application.dto.ResponseUserDto;
import br.com.tick.tickdesck.models.user.application.dto.UpdateUserDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<?> createUser(@Valid @RequestBody RegisterUserDto registerUserDto) {

        var result = ResponseUserDto.fromUser(this.userService.createUser(registerUserDto));
        return ResponseEntity.status(HttpStatus.CREATED).body(result);

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

        var token = this.authUserService.execute(authUserRequestDto);
        return ResponseEntity.status(HttpStatus.OK).body(token);

    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateUser( @PathVariable Long id, @Valid   @RequestBody UpdateUserDto updateUserDto) {
        var result = ResponseUserDto.fromUser(this.userService.updateUser(id, updateUserDto)) ;
        return ResponseEntity.status(HttpStatus.OK).body(result);

    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {

        this.userService.deleteUser(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Usuário deletado com sucesso");

    }

    @GetMapping("/get")
    public ResponseEntity<?> getAllUsers() {
        var result = this.userService.getAll().stream().map(ResponseUserDto::fromUser).toList();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        var result = ResponseUserDto.fromUser(this.userService.getById(id));
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }


}
