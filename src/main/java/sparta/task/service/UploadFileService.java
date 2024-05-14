package sparta.task.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sparta.task.dto.response.UploadFileResponseDto;
import sparta.task.exception.ErrorCode;
import sparta.task.exception.exceptions.HttpStatusException;
import sparta.task.mapper.UploadFileMapper;
import sparta.task.model.UploadFile;
import sparta.task.repository.UploadFileRepository;
import sparta.task.store.FileStore;

@Service
@RequiredArgsConstructor
public class UploadFileService {
    private final UploadFileRepository uploadFileRepository;
    private final UploadFileMapper uploadFileMapper;
    private final FileStore fileStore;

    public UploadFileResponseDto fileUploadTo(Long taskId, MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return null;
        }
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            throw new HttpStatusException(ErrorCode.INVALID_FILENAME);
        }
        UploadFile uploadFile = this.fileStore.save(file);
        uploadFile.setTaskId(taskId);
        try {
            this.uploadFileRepository.save(uploadFile);
        } catch (Exception e) {
            // 저장 된 파일 삭제
            this.fileStore.delete(uploadFile);
            throw new HttpStatusException(ErrorCode.FILE_UPLOAD_FAILED);
        }
        return this.uploadFileMapper.toUploadFileResponseDto(this.uploadFileRepository.save(uploadFile));
    }

    public UploadFile getByFilename(String filename) {
        return this.uploadFileRepository.findByFilename(filename)
                .orElseThrow(() -> new HttpStatusException(ErrorCode.FILE_NOT_FOUND));
    }

    public void deleteById(Long id) {
        this.uploadFileRepository.deleteById(id);
    }
}