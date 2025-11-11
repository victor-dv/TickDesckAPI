package br.com.tick.tickdesck.models.requisitantes.application;

import br.com.tick.tickdesck.models.requisitantes.domain.RequisitanteEntity;
import br.com.tick.tickdesck.models.requisitantes.repository.RequisitanteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RequisitanteService {
    @Autowired
    private RequisitanteRepository requisitanteRepository;

    public RequisitanteEntity getRequisitanteById(Long id) {
        return requisitanteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Requisitante não encontrado"));
    }


}
