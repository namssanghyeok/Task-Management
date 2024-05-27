package sparta.task.domain.repository;

import org.springframework.web.multipart.MultipartFile;
import sparta.task.domain.model.UploadFile;

import java.util.Optional;

public interface UploadFileRepository {
    Optional<UploadFile> findByFilename(String filename);
    UploadFile getByFilename(String filename);
    Optional<UploadFile> findById(Long id);
    UploadFile getById(Long id);

    UploadFile save(UploadFile uploadFile);

    void deleteById(Long id);
}
