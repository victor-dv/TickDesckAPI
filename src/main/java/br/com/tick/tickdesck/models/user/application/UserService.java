package br.com.tick.tickdesck.models.user.application;

import br.com.tick.tickdesck.models.user.application.dto.Role;
import br.com.tick.tickdesck.models.user.application.dto.UpdateUserDto;
import br.com.tick.tickdesck.models.user.domain.UserEntity;
import br.com.tick.tickdesck.models.user.infra.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
        var loggedUser = userRepository.findById(Long.decode((String) authentication.getName()))
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
        return userRepository.save(userEntity);
    }

    /*Função para atualizar as informações do usuario
     * Pegamos o user através do id
     * validamos a existencia do proprio
     * após a validação pegamos as informações passadas e setamos nos campos antigos
     * salvamos no repositorio*/
    public UserEntity updateUser(Long id, UpdateUserDto updateUserDto) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        user.setName(updateUserDto.name() != null ? updateUserDto.name() : user.getName());
        user.setUsername(updateUserDto.username() != null ? updateUserDto.username() : user.getUsername());
        user.setEmail(updateUserDto.email() != null ? updateUserDto.email() : user.getEmail());
        user.setRole(updateUserDto.role() != null ? updateUserDto.role() : user.getRole());
        if (updateUserDto.password() != null) {
            user.setPassword(passwordEncoder.encode(updateUserDto.password()));
        }
        userRepository.findByEmailOrUsername(user.getEmail(), user.getUsername())
                .ifPresent(existingUser -> {
                    if (!existingUser.getId().equals(user.getId())) {
                        throw new RuntimeException("Email ou nome de usuário já está em uso");
                    }
                });

        return userRepository.save(user);
    }

    /*Função para deletar um usuário
     * Verifica se o usuário autenticado tem papel ADMIN ou GERENTE
     * Se não tiver, lança uma exceção
     * Se tiver, busca o usuário pelo id
     * Se o usuário não for encontrado, lança uma exceção
     * Se for encontrado, deleta o usuário do repositório
     * Retorna o usuário deletado */
    public UserEntity deleteUser(Long id) {
        //Pegando o usuario autenticado
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        //Buscando o usuario autenticado no banco de dados
        var loggedUser = userRepository.findById(Long.decode((String) authentication.getName()))
                .orElseThrow(() -> new RuntimeException("Usuário autenticado não encontrado"));
        //Verificando se o usuário tem papel ADMIN ou GERENTE
        if (!loggedUser.getRole().equals(Role.ADMIN) && !loggedUser.getRole().equals(Role.GERENT)) {
            throw new RuntimeException("Apenas usuários com papel ADMIN ou GERENTE podem deletar usuários");
        }
        //Buscando o usuário pelo id
        var user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        //Deletando o usuário
        userRepository.delete(user);
        return user;
    }

}

