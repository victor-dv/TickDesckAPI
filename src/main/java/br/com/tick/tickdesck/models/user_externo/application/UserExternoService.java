package br.com.tick.tickdesck.models.user_externo.application;

import br.com.tick.tickdesck.models.user_externo.domain.UserExternoEntity;
import br.com.tick.tickdesck.models.user_externo.infra.UserExternoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserExternoService {

    @Autowired
    private UserExternoRepository userExternoRepository;

    /**
     * Busca um usuário externo por ID
     */
    public UserExternoEntity getUserById(Long id) {
        return userExternoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário externo não encontrado"));
    }

    /**
     * Lista todos os usuários externos
     */
    public List<UserExternoEntity> getAll() {
        return userExternoRepository.findAll();
    }

}

