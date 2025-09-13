package br.com.tick.tickdesck.models.team.application;

import br.com.tick.tickdesck.models.enterprise.infra.EnterpriseRepository;
import br.com.tick.tickdesck.models.team.application.dto.CreateTeamDto;
import br.com.tick.tickdesck.models.team.domain.TeamEntity;
import br.com.tick.tickdesck.models.team.infra.TeamRepository;
import br.com.tick.tickdesck.models.user.application.dto.Role;
import br.com.tick.tickdesck.models.user.infra.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class TeamService {

    @Autowired
    private EnterpriseRepository enterpriseRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TeamRepository teamRepository;

    public TeamEntity createTeam(CreateTeamDto createTeamDto) {
        //Obtendo o usuário autenticado
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        //Buscando o usuário autenticado no banco de dados
        var loggedUser = userRepository.findById(Long.decode((String) authentication.getName()))
                .orElseThrow(() -> new RuntimeException("Usuário autenticado não encontrado"));
        //Verificando se o usuário tem papel ADMIN ou GERENTE
        if (!loggedUser.getRole().equals(Role.ADMIN) && !loggedUser.getRole().equals(Role.GERENT)) {
            throw new RuntimeException("Apenas usuários com papel ADMIN ou GERENT podem criar uma equipe");
        }

        var enterpise_id = enterpriseRepository.findById(createTeamDto.enterpriseId())
                .orElseThrow(() -> new RuntimeException("Empresa não encontrada"));

        TeamEntity team = new TeamEntity();
        team.setName(createTeamDto.name());
        team.setEnterprise(enterpise_id);
        teamRepository.findByName(team.getName())
                .ifPresent( team1-> {
                    throw new RuntimeException("Essa equipe já existe");
                });

        return teamRepository.save(team);
    }

    public TeamEntity update(Long id, CreateTeamDto createTeamDto) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        //Buscando o usuário autenticado no banco de dados
        var loggedUser = userRepository.findById(Long.decode((String) authentication.getName()))
                .orElseThrow(() -> new RuntimeException("Usuário autenticado não encontrado"));
        //Verificando se o usuário tem papel ADMIN ou GERENTE
        if (!loggedUser.getRole().equals(Role.ADMIN) && !loggedUser.getRole().equals(Role.GERENT)) {
            throw new RuntimeException("Apenas usuários com papel ADMIN ou GERENT podem atualizar uma equipe");
        }

        var team = teamRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Equipe não encontrada"));

        var enterpise_id = enterpriseRepository.findById(createTeamDto.enterpriseId())
                .orElseThrow(() -> new RuntimeException("Empresa não encontrada"));

        team.setName(createTeamDto.name());
        team.setEnterprise(enterpise_id);

        return teamRepository.save(team);
    }

}
