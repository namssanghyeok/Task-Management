package sparta.task.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sparta.task.exception.ErrorCode;
import sparta.task.exception.exceptions.HttpStatusException;
import sparta.task.model.UploadFile;
import sparta.task.repository.UploadFileRepository;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UploadFileService {
    private final UploadFileRepository uploadFileRepository;
    @Value("${file.dir}")
    private String fileDir;

    public UploadFile fileUploadTo(Long taskId, MultipartFile file) {
        return this.storeFile(taskId, file);
    }

    private UploadFile storeFile(Long taskId, MultipartFile file) {
        if (file.isEmpty()) {
            return null;
        }
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            throw new HttpStatusException(ErrorCode.BAD_REQUEST);
        }
        // 확장자 가져오기

        // file 저장
        String filename =UUID.randomUUID().toString();
        try {
            // 실물 파일 저장
            file.transferTo(new File(this.getFullPath(filename)));
        } catch (IOException e) {
            throw new HttpStatusException(ErrorCode.FILE_UPLOAD_FAILED);
        }
        return this.uploadFileRepository.save(
                UploadFile.builder()
                        .originalFileName(originalFilename)
                        .filename(filename)
                        .taskId(taskId)
                        .createdAt(LocalDateTime.now())
                        .build()
        );
    }

    private String extractExt(String filename) {
        return filename.substring(filename.lastIndexOf("."));
    }

    private String getFullPath(String filename) {
        return fileDir + filename;
    }

}