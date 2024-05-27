package sparta.task.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;
import sparta.task.presentational.web.dto.response.UploadFileResponseDto;
import sparta.task.constants.ErrorCode;
import sparta.task.exception.exceptions.ForbiddenException;
import sparta.task.exception.exceptions.HttpStatusException;
import sparta.task.application.mapper.UploadFileMapper;
import sparta.task.domain.model.Task;
import sparta.task.domain.model.UploadFile;
import sparta.task.domain.model.User;
import sparta.task.infrastructure.repository.jpa.TaskJpaRepository;
import sparta.task.infrastructure.repository.jpa.UploadFileJpaRepository;
import sparta.task.application.store.FileStore;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
@RequiredArgsConstructor
public class FileService {
    private final TaskJpaRepository taskJpaRepository;
    private final UploadFileJpaRepository uploadFileJpaRepository;
    private final UploadFileMapper uploadFileMapper;
    private final FileStore fileStore;

    public UploadFileResponseDto fileUploadTo(long taskId, MultipartFile file) {
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
            return this.uploadFileMapper.toUploadFileResponseDto(this.uploadFileJpaRepository.save(uploadFile));
        } catch (Exception e) {
            // 저장 된 파일 삭제
            this.fileStore.delete(uploadFile);
            throw new HttpStatusException(ErrorCode.FILE_UPLOAD_FAILED);
        }
    }

    public UploadFile getByFilename(String filename) {
        return this.uploadFileJpaRepository.findByFilename(filename)
                .orElseThrow(() -> new HttpStatusException(ErrorCode.FILE_NOT_FOUND));
    }

    public Resource getResource(UploadFile uploadFile) {
        try {
            return new UrlResource(uploadFile.getUrl());
        } catch (MalformedURLException e) {
            throw new HttpStatusException(ErrorCode.FILE_DOWNLOAD_FAILED);
        }
    }

    public ByteArrayResource getByteArrayResource(List<UploadFile> attachments) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // TODO: 비동기로 for문 돌게 수정해야함
        try (ZipOutputStream zos = new ZipOutputStream(baos)) {
            for (UploadFile attachment : attachments) {
                UrlResource res = new UrlResource(attachment.getUrl());
                // NOTE: 파일 이름이 중복이면 안됨
                ZipEntry zipEntry = new ZipEntry(attachment.getId() + "_" + attachment.getOriginalFilename());
                zos.putNextEntry(zipEntry);
                StreamUtils.copy(res.getInputStream(), zos);
                zos.closeEntry();
            }
        } catch (IOException e) {
            throw new HttpStatusException(ErrorCode.FILE_DOWNLOAD_FAILED);
        }
        return new ByteArrayResource(baos.toByteArray(), "application/octet-stream");

    }

    public void deleteById(Long id, User currentUser) {
        UploadFile uploadFile = this.uploadFileJpaRepository.findById(id).orElseThrow(() -> new HttpStatusException(ErrorCode.FILE_NOT_FOUND));
        Task task = this.taskJpaRepository.findById(uploadFile.getTaskId()).orElseThrow(() -> new HttpStatusException(ErrorCode.FILE_NOT_FOUND));
        if(task.canUpdateBy(currentUser)) {
            throw new ForbiddenException();
        }

        this.uploadFileJpaRepository.deleteById(id);
    }
}