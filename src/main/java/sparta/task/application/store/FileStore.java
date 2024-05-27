package sparta.task.application.store;

import org.springframework.web.multipart.MultipartFile;
import sparta.task.domain.model.UploadFile;

public interface FileStore {
    UploadFile save(MultipartFile file);
    String getPath(String filename);
    void delete(String filename);
    void delete(UploadFile uploadFile);
}
