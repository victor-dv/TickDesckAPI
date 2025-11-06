package br.com.tick.tickdesck.security;

import br.com.tick.tickdesck.models.enterprise.domain.EnterpriseEntity;
import br.com.tick.tickdesck.models.enterprise.infra.EnterpriseRepository;
import br.com.tick.tickdesck.models.team.domain.TeamEntity;
import br.com.tick.tickdesck.models.team.infra.TeamRepository;
import br.com.tick.tickdesck.models.user_interno.application.dto.Role;
import br.com.tick.tickdesck.models.user_interno.domain.UserEntity;
import br.com.tick.tickdesck.models.user_interno.infra.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

@Component
public class AdminInitializer implements CommandLineRunner {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private EnterpriseRepository enterpriseRepository;

    @Override
    public void run(String... args) {
        if(enterpriseRepository.findByCnpjOrEmail("", "tickdesck@gmail.com").isEmpty()) {
            EnterpriseEntity enterpriseEntity = new EnterpriseEntity();
            enterpriseEntity.setEmail("tickdesck@gmail.com");
            enterpriseEntity.setPhone("11994399077");
            enterpriseEntity.setCnpj("54506603000199");
            enterpriseEntity.setFantasyName("TickDesk");
            enterpriseEntity.setCorporateName("TickDesk LTDA");
            enterpriseRepository.save(enterpriseEntity);

            TeamEntity teamEntity = new TeamEntity();
            teamEntity.setName("TickDesk LTDA");
            teamEntity.setEnterprise(enterpriseEntity);
            teamRepository.save(teamEntity );

            UserEntity adminUser = new UserEntity();
            adminUser.setName("Administrador TIICKDESCK");
            adminUser.setUsername("ADMIN");
            adminUser.setEmail("admin.tiickdesck@tiickdesck.com");
            adminUser.setPassword(passwordEncoder.encode("tckdesck@123"));
            adminUser.setRole(Role.ADMIN);
            adminUser.setTeamEntity(teamEntity);
            adminUser.setFirstAccess(false);

            userRepository.save(adminUser);
            System.out.println("Usuário administrador criado e configurado com sucesso!");
        }else {
            System.out.println("Usuário administrador já existe.");
        }
    }
}