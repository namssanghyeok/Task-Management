package sparta.task.application.store;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import sparta.task.infrastructure.exception.constants.ErrorCode;
import sparta.task.infrastructure.exception.HttpStatusException;
import sparta.task.domain.model.UploadFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * 실제 파일 저장 / 다운로드
 */
@Component
public class LocalFileStore implements FileStore {
    private final String URL_PREFIX = "file:";
    @Value("${file.dir}")
    private String fileDir;

    @Override
    public UploadFile save(MultipartFile file) {
        String filename = UUID.randomUUID().toString();
        String path = this.getPath(filename);
        try {
            file.transferTo(new File(path));
        } catch (IOException e) {
            throw new HttpStatusException(ErrorCode.FILE_UPLOAD_FAILED);
        }
        return
                UploadFile.builder()
                        .originalFilename(file.getOriginalFilename())
                        .type(file.getContentType())
                        .size(file.getSize())
                        .filename(filename)
                        .url(URL_PREFIX + this.getPath(filename))
                        .build();
    }

    @Override
    public String getPath(String filename) {
        return fileDir + filename;
    }

    // TODO
    @Override
    public void delete(String filename) {

    }

    // TODO
    @Override
    public void delete(UploadFile uploadFile) {

    }
}
