package sparta.task.presentational.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sparta.task.domain.model.UploadFile;
import sparta.task.domain.model.User;
import sparta.task.presentational.web.argumentResolver.annotation.LoginUser;
import sparta.task.application.service.FileService;

@Tag(name = "파일 다운로드 / 삭제 / 일반 요청")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/file")
public class FileController {
    private final FileService fileService;

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFile(@PathVariable long id,
                                        @LoginUser User currentUser
    ) {
        this.fileService.deleteById(id, currentUser);

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @Operation(summary = "파일 다운로드.", description = "header에 disposition을 넣어줌으로써 브라우저가 자동으로 다운로드 하게 만듦")
    @GetMapping(value = "/{filename}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String filename) {
        UploadFile uploadFile = this.fileService.getByFilename(filename);
        Resource resource = this.fileService.getResource(uploadFile);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + uploadFile.getOriginalFilename() + "\"")
                .body(resource);
    }
}
