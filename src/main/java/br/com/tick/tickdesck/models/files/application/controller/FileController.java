package br.com.tick.tickdesck.models.files.application.controller;

import br.com.tick.tickdesck.models.files.application.FileService;
import br.com.tick.tickdesck.models.files.application.dto.FileDto;
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
@RequestMapping("api/files")
public class FileController {

    @Autowired
    private FileService fileService;

    @SneakyThrows
    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile fileData, @RequestParam("actionId") Long actionId) {

        var fileName = fileService.uploadFile(fileData, actionId);
        return ResponseEntity.status(HttpStatus.OK).body(fileName);

    }

    @GetMapping("/filesList")
    public ResponseEntity<?> listFiles() {
        var fileList = fileService.listFiles();
        return ResponseEntity.status(HttpStatus.OK).body(fileList);
    }

    // se a pasta uploads nao existir, criar

}
