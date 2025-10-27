package br.com.tick.tickdesck.security;

import br.com.tick.tickdesck.models.user_interno.application.dto.Role;
import br.com.tick.tickdesck.models.user_interno.domain.UserEntity;
import br.com.tick.tickdesck.models.user_interno.infra.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminInitializer implements CommandLineRunner {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public void run(String... args) {

        if (userRepository.findByEmail("admin.tiickdesck@tiickdesck.com").isEmpty()) {
            UserEntity adminUser = new UserEntity();
            adminUser.setName("Administrador TIICKDESCK");
            adminUser.setUsername("ADMIN");
            adminUser.setEmail("admin.tiickdesck@tiickdesck.com");
            adminUser.setPassword(passwordEncoder.encode("tckdesck@123"));
            adminUser.setRole(Role.ADMIN);
            adminUser.setTeamEntity(null);
            adminUser.setFirstAccess(false);

            userRepository.save(adminUser);
            System.out.println("Usuário administrador criado com sucesso!");

        } else {
            System.out.println("Usuário administrador já existe.");
        }
    }
}