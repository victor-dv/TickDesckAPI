package br.com.tick.tickdesck.models.files.application;

import br.com.tick.tickdesck.models.auditoria_call.domain.ActionEntity;
import br.com.tick.tickdesck.models.auditoria_call.repository.ActionRepository;
import br.com.tick.tickdesck.models.files.domain.FileEntity;
import br.com.tick.tickdesck.models.files.infra.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class FileService {

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private ActionRepository actionRepository;

    public List<FileEntity> listFiles() {

        return fileRepository.findAll();
    }

    public FileEntity uploadFile(MultipartFile fileData, Long actionId) throws IOException {

        //// Busca a entidade Actions pelo ID fornecido, lançando exceção se não existir
        ActionEntity action = actionRepository.findById(actionId)
                .orElseThrow(() -> new RuntimeException("Action não encontrada com Id: " + actionId));

        // Define the upload directory path based on the company, call, and action IDs
        Path uploadDir = Paths.get("uploads/Enterprise/" + action.getCallsEntity().getTeam().getEnterprise().getId() + "/chamados/" + action.getCallsEntity().getId() + "/acoes/" + action.getId() + "/");
        // Checks if the directory does not exist
        if (!Files.exists(uploadDir)) {
            // Creates the directory and any nonexistent parent directories
            Files.createDirectories(uploadDir);
        }

        //// Obtém o nome original do arquivo enviado
        String fileName = fileData.getOriginalFilename();
        //// Lê o conteúdo do arquivo enviado como array de bytes
        byte[] fileContent = fileData.getBytes();

        //// Define o caminho onde o arquivo será salvo na pasta uploads
        Path filePath = uploadDir.resolve(fileName);
        //// Escreve o conteúdo do arquivo no caminho definido
        Files.write(filePath, fileContent);

        //// Cria uma nova entidade FileEntity e preenche seus campos com as informações do arquivo e da ação
        FileEntity archive = new FileEntity();//objeto
        archive.setName(fileName);
        archive.setType(fileData.getContentType());
        archive.setPath(filePath.toString());
        archive.setAction(action);

        return fileRepository.save(archive);
    }

    public Resource downloadFile(Long id) {
        try {
            FileEntity fileEntity = fileRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("File not found with id: " + id));
            Path path = Paths.get(fileEntity.getPath());

            Resource resource = new UrlResource(path.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
    }