package br.com.tick.tickdesck.models.files.infra;

import br.com.tick.tickdesck.models.files.domain.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FileRepository extends JpaRepository<FileEntity, Long> {


    Optional<FileEntity> findByname(String fileName);
}
