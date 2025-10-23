package br.com.tick.tickdesck.models.user.application;

import br.com.tick.tickdesck.models.emails.EmailService;
import br.com.tick.tickdesck.models.password.application.GeneratedPasswordService;
import br.com.tick.tickdesck.models.password.application.dto.UpdatePasswordDto;
import br.com.tick.tickdesck.models.team.infra.TeamRepository;
import br.com.tick.tickdesck.models.user.application.dto.RegisterUserDto;
import br.com.tick.tickdesck.models.user.application.dto.Role;
import br.com.tick.tickdesck.models.user.application.dto.UpdateUserDto;
import br.com.tick.tickdesck.models.user.domain.UserEntity;
import br.com.tick.tickdesck.models.user.infra.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    //Conectando a classe UserService no UserRepositoru
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private EmailService emailService;


    /*Função para criar um usuário
    *Verifica se o usuário já existe pelo username ou email
    *Se existir, lança uma exceção
    *Se não existir, salva o usuário no repositório
    Retorna o usuário criado */
    public UserEntity createUser(RegisterUserDto registerUserDto) {
        // Pegando o usuário autenticado
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        // Buscando o usuário autenticado no banco de dados
        var loggedUser = userRepository.findById(Long.parseLong(authentication.getName()))
                .orElseThrow(() -> new RuntimeException("Usuário autenticado não encontrado"));

        // Checando permissões
        if (!loggedUser.getRole().equals(Role.ADMIN) && !loggedUser.getRole().equals(Role.GERENT)) {
            throw new RuntimeException("Apenas usuários com papel ADMIN ou GERENTE podem criar novos usuários");
        }

        // Verificando se o usuário já existe pelo username ou email
        userRepository.findByEmailOrUsername(registerUserDto.email(), registerUserDto.username())
                .ifPresent(user -> {
                    throw new RuntimeException("Usuário já existe");
                });

        // Criando a entidade do usuário
        UserEntity userEntity = new UserEntity();
        userEntity.setName(registerUserDto.name());
        userEntity.setUsername(registerUserDto.username());
        userEntity.setEmail(registerUserDto.email());
        userEntity.setRole(registerUserDto.role() != null ? registerUserDto.role() : Role.CLIENT);

        // 🔹 Gerar senha temporária e criptografar
        String tempPassword = GeneratedPasswordService.generateRandomPassword(10);
        userEntity.setPassword(passwordEncoder.encode(tempPassword));

        // 🔹 Marcar como primeiro acesso
        userEntity.setFirstAccess(true);

        // Buscando e atribuindo o time
        var team = teamRepository.findById(registerUserDto.teamId())
                .orElseThrow(() -> new RuntimeException("Time não encontrado"));
        userEntity.setTeamEntity(team);

        // Salvando o usuário no repositório
        var savedUser = userRepository.save(userEntity);

        // 🔹 Enviar e-mail com a senha
        emailService.send(
                savedUser.getEmail(),
                "Acesso ao Sistema de Chamados",
                """
                Olá, %s!
        
                Sua conta foi criada com sucesso.
        
                Login: %s
                Senha temporária: %s
        
                Por favor, altere sua senha no primeiro acesso.
                """.formatted(savedUser.getName(), savedUser.getUsername(), tempPassword)
        );

        return savedUser;
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

        var team = teamRepository.findById(updateUserDto.teamEntity().longValue())
                .orElseThrow(() -> new RuntimeException("Time não encontrado"));
        user.setTeamEntity(team);

        userRepository.findByEmailOrUsername(user.getEmail(), user.getUsername())
                .ifPresent(existingUser -> {
                    if (!existingUser.getId().equals(user.getId())) {
                        throw new RuntimeException("Email ou nome de usuário já está em uso");
                    }
                });

        return userRepository.save(user);
    }

    /* Função para deletar um usuário
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

    public List<UserEntity> getAll() {
        return userRepository.findAll();
    }

    public UserEntity getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    public void updatePassword(Long id, UpdatePasswordDto dto) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (!passwordEncoder.matches(dto.oldPassword(), user.getPassword())) {
            throw new RuntimeException("Senha antiga incorreta");
        }

        user.setPassword(passwordEncoder.encode(dto.newPassword()));
        user.setFirstAccess(false);

        userRepository.save(user);
    }

}

