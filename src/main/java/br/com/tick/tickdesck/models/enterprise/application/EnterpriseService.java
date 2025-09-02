package br.com.tick.tickdesck.models.enterprise.application;

import br.com.tick.tickdesck.models.enterprise.application.dto.CreateEnterpriseDto;
import br.com.tick.tickdesck.models.enterprise.domain.EnterpriseEntity;
import br.com.tick.tickdesck.models.enterprise.infra.EnterpriseRepository;
import br.com.tick.tickdesck.models.user.application.dto.Role;
import br.com.tick.tickdesck.models.user.infra.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

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
        return enterpriseRepository.save(enterprise);
    }

}
