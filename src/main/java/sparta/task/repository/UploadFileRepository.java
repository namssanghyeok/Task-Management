package sparta.task.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sparta.task.model.UploadFile;

import java.util.Optional;

public interface UploadFileRepository extends JpaRepository<UploadFile, Long> {
    Optional<UploadFile> findByFilename(String fileName);
}
