package br.com.tick.tickdesck.api.enterprise;

import br.com.tick.tickdesck.models.enterprise.application.EnterpriseService;
import br.com.tick.tickdesck.models.enterprise.application.dto.CreateEnterpriseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/enterprise")
public class EnterpriseController {

    @Autowired
    private EnterpriseService enterpriseService;

    @PostMapping("/create")
    public ResponseEntity<?> createEnterprise(@RequestBody CreateEnterpriseDto createEnterpriseDto) {
        try {
            var result = this.enterpriseService.create(createEnterpriseDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } catch (RuntimeException e) {
            throw new RuntimeException("Erro ao criar empresa: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ocorreu um erro interno no servidor: " + e.getMessage());
        }

    }

}
