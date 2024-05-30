package sparta.task.domain.store;

import org.springframework.web.multipart.MultipartFile;
import sparta.task.domain.model.UploadFile;

/**
 * 파일 업로드 인터페이스
 *
 * <p>아래의 4개의 인터페이스를 제공해야한다.</p>
 */
public interface FileStore {
    UploadFile save(MultipartFile file);

    String getPath(String filename);

    void delete(String filename);

    void delete(UploadFile uploadFile);
}
