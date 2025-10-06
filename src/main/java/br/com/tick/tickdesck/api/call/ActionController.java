package br.com.tick.tickdesck.api.call;

import br.com.tick.tickdesck.models.action_call.application.ActionService;
import br.com.tick.tickdesck.models.action_call.application.dto.ActionDto;
import br.com.tick.tickdesck.models.action_call.application.dto.FileDto;
import br.com.tick.tickdesck.models.action_call.domain.Actions;
import br.com.tick.tickdesck.models.files.application.FileService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("api/calls/actions")
public class ActionController {

    @Autowired
    private ActionService actionService;


    @Autowired
    private FileService fileService;


    @PostMapping("/")
    public ResponseEntity<?> createAction(@RequestBody ActionDto actionDto) {

        var result = this.actionService.createAction(actionDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);

    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAction(@PathVariable Long id) {


        var result = this.actionService.getAction(id);
        var response = new ActionDto(
                result.getIdChamado(),
                result.getId(),
                result.getDescription(),
                result.getPublica(),
                result.getDataCadastro()
                , result.getFile().stream().map(file -> {
                    Path path = Paths.get(file.getPath());
                    boolean exists = Files.exists(path);
                    if (!exists) {
                        file.setPath("File not found");
                    }
                    return new FileDto(
                            file.getId(),
                            file.getName(),
                            file.getType(),
                            path.toUri().toString()
                    );
                }).toList()

        );
        return ResponseEntity.status(HttpStatus.OK).body(response);

    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateAction(@PathVariable Long id, @RequestBody Actions updatedAction) {

        var result = this.actionService.updateAction(id, updatedAction);
        return ResponseEntity.status(HttpStatus.OK).body(result);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAction(@PathVariable Long id) {

        var result = this.actionService.deleteAction(id);
        return ResponseEntity.status(HttpStatus.OK).body(result);

    }


    @SneakyThrows
    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile fileData, @RequestParam("actionId") Long actionId) {

        var fileName = fileService.uploadFile(fileData, actionId);
        return ResponseEntity.status(HttpStatus.OK).body(fileName);

    }

    @GetMapping("/filesList")
    public ResponseEntity<?> listFiles() {
        // Obtém a lista de arquivos cadastrados pelo serviço de arquivos.
        var files = fileService.listFiles();
        // Cria uma lista de FileDto a partir dos arquivos encontrados.
        var response = files.stream()
                // Para cada arquivo:
                .map(file -> {
                    // Obtém o caminho do arquivo no sistema de arquivos.
                    Path path = Paths.get(file.getPath());
                    // Verifica se o arquivo existe no disco.
                    boolean exists = Files.exists(path);
                    try {
                        // Cria um recurso do Spring a partir do caminho do arquivo.
                        Resource resource = new UrlResource(path.toUri());
                        // Retorna um DTO com os dados do arquivo e o caminho (ou "File not found" se não existir).
                        return new FileDto(file.getId(), file.getName(), file.getType(), exists ? path.toUri().toString() : "File not found");
                    } catch (MalformedURLException e) {
                        // Em caso de erro ao criar o recurso, lança uma exceção em tempo de execução.
                        throw new RuntimeException(e);
                    }

                })
                // Coleta todos os DTOs em uma lista.
                .toList();
        return ResponseEntity.status(HttpStatus.OK).body(response);

    }

    // se a pasta uploads nao existir, criar

}
