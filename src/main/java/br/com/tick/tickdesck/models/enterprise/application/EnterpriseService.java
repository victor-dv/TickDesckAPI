package br.com.tick.tickdesck.models.enterprise.application;

import br.com.tick.tickdesck.models.enterprise.application.dto.CreateEnterpriseDto;
import br.com.tick.tickdesck.models.enterprise.application.dto.UpdateEnterpriseDto;
import br.com.tick.tickdesck.models.enterprise.domain.EnterpriseEntity;
import br.com.tick.tickdesck.models.enterprise.infra.EnterpriseRepository;
import br.com.tick.tickdesck.models.team.domain.TeamEntity;
import br.com.tick.tickdesck.models.user_interno.application.dto.Role;
import br.com.tick.tickdesck.models.user_interno.domain.UserEntity;
import br.com.tick.tickdesck.models.user_interno.infra.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnterpriseService {


    @Autowired
    private EnterpriseRepository enterpriseRepository;
    @Autowired
    private UserRepository userRepository;


    public EnterpriseEntity create(CreateEnterpriseDto createEnterpriseDto) {
        //Obtendo o usuário autenticado
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        //Buscando o usuário autenticado no banco de dados
        var loggedUser = userRepository.findById(Long.decode((String) authentication.getName()))
                .orElseThrow(() -> new RuntimeException("Usuário autenticado não encontrado"));
        //Verificando se o usuário tem papel ADMIN ou GERENTE
        if (!loggedUser.getRole().equals(Role.ADMIN)) {
            throw new RuntimeException("Apenas usuários com papel ADMIN podem criar uma empresa");
        }

        EnterpriseEntity enterprise = new EnterpriseEntity();
        enterprise.setEmail(createEnterpriseDto.email());
        enterprise.setCorporateName(createEnterpriseDto.corporateName());
        enterprise.setFantasyName(createEnterpriseDto.fantasyName());
        enterprise.setCnpj(createEnterpriseDto.cnpj());
        enterprise.setPhone(createEnterpriseDto.phone());

        enterpriseRepository.findByCnpjOrEmail(enterprise.getCnpj(), enterprise.getEmail())
                .ifPresent(enterpre -> {
                    throw new RuntimeException("Empresa com cnpj ou email já cadastrados");
                });
        return enterpriseRepository.save(enterprise);
    }

    /*
     * Função para atualizar as informações da empresa
     * Pegamos a empresa através do id
     * validamos a existencia da propria
     * após a validação pegamos as informações passadas e setamos nos campos antigos
     * salvamos no repositorio
     * */
    public EnterpriseEntity update(Long id, UpdateEnterpriseDto updateEnterpriseDto) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        //Buscando o usuário autenticado no banco de dados
        var loggedUser = userRepository.findById(Long.decode((String) authentication.getName()))
                .orElseThrow(() -> new RuntimeException("Usuário autenticado não encontrado"));
        //Verificando se o usuário tem papel ADMIN ou GERENTE
        if (!loggedUser.getRole().equals(Role.ADMIN) && !loggedUser.getRole().equals(Role.GERENT)) {
            throw new RuntimeException("Apenas usuários com papel ADMIN e GERENT podem atualizar uma empresa");
        }

        var enterprise = enterpriseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Empresa não encontrada"));

        enterprise.setEmail(updateEnterpriseDto.email() != null ? updateEnterpriseDto.email() : enterprise.getEmail());
        enterprise.setFantasyName(updateEnterpriseDto.fantasyName() != null ? updateEnterpriseDto.fantasyName() : enterprise.getFantasyName());
        enterprise.setPhone(updateEnterpriseDto.phone() != null ? updateEnterpriseDto.phone() : enterprise.getPhone());

        enterpriseRepository.findByEmailOrCnpjOrPhone(enterprise.getEmail(), enterprise.getCnpj(), enterprise.getPhone())
                .ifPresent(enterpre -> {
                    if (!enterpre.getId().equals(enterprise.getId())) {
                        throw new RuntimeException("Empresa com email, cnpj ou phone  já cadastrados");
                    }
                });
        return enterpriseRepository.save(enterprise);
    }


    public EnterpriseEntity delete(Long id) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        //Buscando o usuário autenticado no banco de dados
        var loggedUser = userRepository.findById(Long.decode((String) authentication.getName()))
                .orElseThrow(() -> new RuntimeException("Usuário autenticado não encontrado"));
        //Verificando se o usuário tem papel ADMIN ou GERENTE
        if (!loggedUser.getRole().equals(Role.ADMIN)) {
            throw new RuntimeException("Apenas usuários com papel ADMIN podem deletar uma empresa");
        }

        var enterprise = enterpriseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Empresa não encontrada"));
        enterpriseRepository.delete(enterprise);
        return enterprise;
    }

    public List<EnterpriseEntity> getAll() {
        return enterpriseRepository.findAll();
    }

    public List <TeamEntity> getTeamsByEnterpriseId(Long id){
        var enterprise = enterpriseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Empresa não encontrada"));
        return enterprise.getTeams();
    }

    public List<UserEntity> getUsersByEnterpriseId(Long id){
            if (!enterpriseRepository.existsById(id)) {
                throw new RuntimeException("Empresa não encontrada");
            }
            return userRepository.findByEnterpriseId(id);
    }

}
