package br.com.tick.tickdesck.api.enterprise;

import br.com.tick.tickdesck.models.enterprise.application.EnterpriseService;
import br.com.tick.tickdesck.models.enterprise.application.dto.CreateEnterpriseDto;
import jakarta.validation.Valid;
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
    public ResponseEntity<?> createEnterprise(@Valid @RequestBody CreateEnterpriseDto createEnterpriseDto) {
        var result = this.enterpriseService.create(createEnterpriseDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }
}
