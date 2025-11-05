package br.com.tick.tickdesck.models.files.infra;

import br.com.tick.tickdesck.models.files.domain.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FileRepository extends JpaRepository<FileEntity, Long> {


    Optional<FileEntity> findByname(String name);

}
