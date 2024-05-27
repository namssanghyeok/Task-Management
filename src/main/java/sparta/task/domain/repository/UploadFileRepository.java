package sparta.task.domain.repository;

import sparta.task.domain.model.UploadFile;

public interface UploadFileRepository {
    UploadFile getByFilename(String filename);

    UploadFile getById(Long id);

    UploadFile save(UploadFile uploadFile);

    void deleteById(Long id);
}
