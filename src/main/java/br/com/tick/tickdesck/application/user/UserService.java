package br.com.tick.tickdesck.application.user;

import br.com.tick.tickdesck.domain.user.UserEntity;
import br.com.tick.tickdesck.infraestructure.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    //Conectando a classe UserService no UserRepositoru
    @Autowired
    private UserRepository userRepository;

    //Função para criar um usuário
    //Verifica se o usuário já existe pelo username ou email
    //Se existir, lança uma exceção
    //Se não existir, salva o usuário no repositório
    //Retorna o usuário criado
    public UserEntity createUser(UserEntity userEntity) {
        this.userRepository
                .findByUsernameOrEmail(userEntity.getEmail(), userEntity.getUsername())
                .ifPresent((user) -> {
                    throw new RuntimeException("User already exists with username or email: " + userEntity.getUsername() + " / " + userEntity.getEmail());
                });

        return this.userRepository.save(userEntity);
    }

}

