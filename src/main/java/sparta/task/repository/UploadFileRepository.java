package sparta.task.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sparta.task.model.UploadFile;

public interface UploadFileRepository extends JpaRepository<UploadFile, Long> {
}
