package br.com.tick.tickdesck.application.user;

import br.com.tick.tickdesck.application.dto.Role;
import br.com.tick.tickdesck.domain.user.UserEntity;
import br.com.tick.tickdesck.infraestructure.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {

    //Conectando a classe UserService no UserRepositoru
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /*Função para criar um usuário
    *Verifica se o usuário já existe pelo username ou email
    *Se existir, lança uma exceção
    *Se não existir, salva o usuário no repositório
    Retorna o usuário criado */
    public UserEntity createUser(UserEntity userEntity) {
        //Pegando o usuario autenticado
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        //Buscando o usuario autenticado no banco de dados
        var loggedUser = userRepository.findById(Long.decode((String)authentication.getName()))
                .orElseThrow(() -> new RuntimeException("Usuário autenticado não encontrado"));

        if (!loggedUser.getRole().equals(Role.ADMIN) && !loggedUser.getRole().equals(Role.GERENT)) {
            throw new RuntimeException("Apenas usuários com papel ADMIN ou GERENTE podem criar novos usuários");
        }
        //Verificando se o usuário já existe pelo username ou email
        userRepository.findByEmailOrUsername(userEntity.getEmail(), userEntity.getUsername())
                .ifPresent(user -> {
                    throw new RuntimeException("Usuário já existe");
                });

        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));

        //Se o papel do usuário não for definido, define como ADMIN por padrão
        if (userEntity.getRole() == null) {
            userEntity.setRole(Role.CLIENT);
        }

        //Salvando o usuário no repositório
        return this.userRepository.save(userEntity);
    }

}

