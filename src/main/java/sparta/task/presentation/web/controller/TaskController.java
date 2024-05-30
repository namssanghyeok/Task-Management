package sparta.task.presentation.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sparta.task.application.usecase.FileUseCase;
import sparta.task.application.usecase.TaskUseCase;
import sparta.task.domain.model.User;
import sparta.task.application.dto.request.CreateTaskRequestDto;
import sparta.task.application.dto.request.UpdateTaskRequestDto;
import sparta.task.application.dto.request.UploadFileRequestDto;
import sparta.task.application.dto.response.TaskResponseDto;
import sparta.task.presentation.web.exception.CustomErrorResponse;
import sparta.task.presentation.web.argumentResolver.annotation.LoginUser;

@Slf4j
@Tag(name = "Task Controller", description = "Task 컨트롤러입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/task")
public class TaskController {
    private final TaskUseCase taskUseCase;
    private final FileUseCase fileUseCase;

    @GetMapping("/test")
    ResponseEntity<?> temp(@LoginUser User currentUser) {
        System.out.println(currentUser);
        if (currentUser != null) {
            System.out.println(currentUser.getUsername());
        }
        return ResponseEntity.ok("hello world");
    }

    @GetMapping("/user")
    ResponseEntity<?> user(@LoginUser User currentUser) {
        System.out.println(currentUser);
        if (currentUser != null) {
            System.out.println(currentUser.getUsername());
        }
        return ResponseEntity.ok("user");
    }

    @GetMapping("/admin")
    ResponseEntity<?> admin(@LoginUser User currentUser) {
        System.out.println(currentUser);
        if (currentUser != null) {
            System.out.println(currentUser.getUsername());
        }
        return ResponseEntity.ok("admin");
    }

    @Operation(summary = "task를 생성합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", content = @Content(schema = @Schema(implementation = TaskResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "validation 오류"),
    })
    @PostMapping
    ResponseEntity<?> createTask(@Valid @RequestBody CreateTaskRequestDto createTaskRequestDto,
                                 @LoginUser User currentUser
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(taskUseCase.createTask(createTaskRequestDto, currentUser));
    }

    @Operation(summary = "task를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = TaskResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "삭제된 task"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 task"),
    })
    @GetMapping("/{id}")
    ResponseEntity<?> showTaskById(@PathVariable Long id) {
        return ResponseEntity.ok(this.taskUseCase.getById(id));
    }

    @Operation(summary = "모든 task를 조회합니다.")
    @ApiResponse(responseCode = "200", content = @Content(array = @ArraySchema(schema = @Schema(implementation = TaskResponseDto.class))), description = "삭제되지 않은 모든 task를 시간순(desc)으로 정렬합니다.")
    @GetMapping
    ResponseEntity<?> showAllTasks() {
        return ResponseEntity.ok(this.taskUseCase.showAll());
    }

    @Operation(summary = "task를 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = TaskResponseDto.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "validation error", content = @Content(schema = @Schema(implementation = CustomErrorResponse.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "password error", content = @Content(schema = @Schema(implementation = CustomErrorResponse.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "not found", content = @Content(schema = @Schema(implementation = CustomErrorResponse.class), mediaType = "application/json"))
    })
    @PutMapping("/{id}")
    ResponseEntity<?> updateTask(
            @PathVariable Long id,
            @Valid @RequestBody UpdateTaskRequestDto updateTaskRequestDto,
            @LoginUser User currentUser
    ) {
        return ResponseEntity.ok(this.taskUseCase.updateTaskBy(id, updateTaskRequestDto, currentUser));
    }

    @Operation(summary = "task를 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "delete success"),
            @ApiResponse(responseCode = "400", description = "already deleted", content = @Content(schema = @Schema(implementation = CustomErrorResponse.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "password error", content = @Content(schema = @Schema(implementation = CustomErrorResponse.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "not found", content = @Content(schema = @Schema(implementation = CustomErrorResponse.class), mediaType = "application/json"))
    })
    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteTask(@PathVariable Long id, @LoginUser User currentUser) {
        // 예외를 비즈니스 -> 컨틀로러 catch -> 예외를 또 던지면
        //
        this.taskUseCase.deleteBy(id, currentUser);
        //
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "task에 파일을 첨부합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "file upload success"),
            @ApiResponse(responseCode = "400", description = "validation error"),
            @ApiResponse(responseCode = "403", description = "password error"),
            @ApiResponse(responseCode = "404", description = "task not found")
    })
    @PostMapping("/{id}/attachment/upload")
    public ResponseEntity<?> uploadFile(@PathVariable Long id,
                                        @Valid @ModelAttribute UploadFileRequestDto uploadFileRequestDto,
                                        @LoginUser User currentUser
    ) {
        // this.taskService.findByIdAndCheckCanUpdate(id, currentUser);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(this.fileUseCase.fileUploadTo(id, uploadFileRequestDto.getFile()));
    }

    @Operation(summary = "task에 업로드 된 파일을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = @Content(array = @ArraySchema(schema = @Schema(implementation = UploadFileRequestDto.class)))),
            @ApiResponse(responseCode = "404", description = "task not found")
    })
    @GetMapping("/{id}/attachment")
    public ResponseEntity<?> showFiles(@PathVariable Long id) {
        return ResponseEntity.ok(this.taskUseCase.findUploadFilesByTaskId(id));
    }

    @Operation(summary = "task에 업로드 된 모든 파일 zip 형태로 다운로드.")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "404", description = "task not found"),
            @ApiResponse(responseCode = "500", description = "server error")
    })
    @GetMapping("/{id}/attachment/download")
    public ResponseEntity<?> downloadAllAttachments(@PathVariable Long id) {
//        Task task = this.taskUseCase.getById(id);
//        List<UploadFile> attachments = task.getAttachments();
//        if (attachments == null || attachments.isEmpty()) {
//            throw new HttpStatusException(ErrorCode.EMPTY_FILES);
//        }
//        return ResponseEntity.ok()
//                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=files.zip")
//                .body(this.fileUseCase.getByteArrayResource(attachments));
        return ResponseEntity.ok("수정해야함");
    }

}
