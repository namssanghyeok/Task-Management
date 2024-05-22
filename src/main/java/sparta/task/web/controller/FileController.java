package sparta.task.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sparta.task.dto.request.PasswordRequestDto;
import sparta.task.model.UploadFile;
import sparta.task.service.FileService;
import sparta.task.service.TaskService;

@Tag(name = "파일 다운로드 / 삭제 / 일반 요청")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/file")
public class FileController {
    private final TaskService taskService;
    private final FileService fileService;

    @DeleteMapping("/{filename}")
    public ResponseEntity<?> deleteFile(@PathVariable String filename,
                                        @Valid @ModelAttribute PasswordRequestDto passwordRequestDto
    ) {
        UploadFile uploadFile = this.fileService.getByFilename(filename);
        Long taskId = uploadFile.getTaskId();
        this.taskService.findByIdAndCheckPassword(taskId, passwordRequestDto.getPassword());
        this.fileService.deleteById(uploadFile.getId());

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
