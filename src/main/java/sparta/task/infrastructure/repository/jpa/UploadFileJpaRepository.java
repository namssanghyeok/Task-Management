package sparta.task.infrastructure.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import sparta.task.domain.model.UploadFile;

import java.util.Optional;

public interface UploadFileJpaRepository extends JpaRepository<UploadFile, Long> {
    Optional<UploadFile> findByFilename(String fileName);
}
