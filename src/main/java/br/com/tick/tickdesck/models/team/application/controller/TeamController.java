package br.com.tick.tickdesck.models.team.application.controller;

import br.com.tick.tickdesck.models.team.application.TeamService;
import br.com.tick.tickdesck.models.team.application.dto.CreateTeamDto;
import br.com.tick.tickdesck.models.team.application.dto.ResponseTeamDto;
import br.com.tick.tickdesck.models.user.application.dto.ResponseUserDto;
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
        var result = ResponseTeamDto.fromTeamEntity(this.teamService.createTeam(createTeamDto));
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateTeam(@PathVariable Long id, @RequestBody CreateTeamDto createTeamDto) {
        var result = ResponseTeamDto.fromTeamEntity(this.teamService.update(id, createTeamDto));
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteTeam(@PathVariable Long id) {
        this.teamService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/get")
    public ResponseEntity<?> getAllTeams() {
        var result = this.teamService.getAll().stream().map(ResponseTeamDto ::fromTeamEntity).toList();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?> getTeamId(@PathVariable Long id) {
        var result = ResponseTeamDto.fromTeamEntity(this.teamService.getTeamsId(id));
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/get-user/{id}")
    public ResponseEntity<?> getTeamMembersById(@PathVariable Long id) {
        var team = teamService.getTeamMembers(id)
                .stream()
                .map(ResponseUserDto::fromUser)
                .toList();
        return ResponseEntity.status(HttpStatus.OK).body(team);
    }
}
