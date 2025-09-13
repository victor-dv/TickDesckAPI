package br.com.tick.tickdesck.api.team;

import br.com.tick.tickdesck.models.team.application.TeamService;
import br.com.tick.tickdesck.models.team.application.dto.CreateTeamDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/team")
public class TeamController {

    @Autowired
    private TeamService teamService;

    @PostMapping("/create")
    public ResponseEntity<?> createTeam(@Valid @RequestBody CreateTeamDto createTeamDto) {
        var result = this.teamService.createTeam(createTeamDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateTeam(@PathVariable Long id, @RequestBody CreateTeamDto createTeamDto) {
        var result = this.teamService.update(id, createTeamDto);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteTeam(@PathVariable Long id) {
        var result = this.teamService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
