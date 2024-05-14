package sparta.task.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sparta.task.dto.response.UploadFileResponseDto;
import sparta.task.exception.ErrorCode;
import sparta.task.exception.exceptions.HttpStatusException;
import sparta.task.mapper.UploadFileMapper;
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
    private final UploadFileMapper uploadFileMapper;

    @Value("${file.dir}")
    private String fileDir;

    public UploadFileResponseDto fileUploadTo(Long taskId, MultipartFile file) {
        return this.uploadFileMapper.toUploadFileResponseDto(this.storeFile(taskId, file));
    }

    private UploadFile storeFile(Long taskId, MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return null;
        }
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            throw new HttpStatusException(ErrorCode.INVALID_FILENAME);
        }
        String filename = UUID.randomUUID().toString();
        try {
            file.transferTo(new File(this.getFullPath(filename)));
        } catch (IOException e) {
            throw new HttpStatusException(ErrorCode.FILE_UPLOAD_FAILED);
        }
        return this.uploadFileRepository.save(
                UploadFile.builder()
                        .originalFilename(originalFilename)
                        .type(file.getContentType())
                        .size(file.getSize())
                        .filename(filename)
                        .taskId(taskId)
                        .path("file:" + this.getFullPath(filename))
                        .createdAt(LocalDateTime.now())
                        .build()
        );
    }

    public UploadFile getByFilename(String filename) {
        return this.uploadFileRepository.findByFilename(filename)
                .orElseThrow(() -> new HttpStatusException(ErrorCode.FILE_NOT_FOUND));
    }

    public void deleteById(Long id) {
        this.uploadFileRepository.deleteById(id);
    }

    private String getFullPath(String filename) {
        return fileDir + filename;
    }

}