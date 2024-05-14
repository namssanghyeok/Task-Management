package sparta.task.web.controller;

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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sparta.task.dto.request.CreateTaskRequestDto;
import sparta.task.dto.request.DeleteTaskRequestDto;
import sparta.task.dto.request.UpdateTaskRequestDto;
import sparta.task.dto.request.UploadFileRequestDto;
import sparta.task.dto.response.TaskResponseDto;
import sparta.task.exception.CustomErrorResponse;
import sparta.task.model.Task;
import sparta.task.service.TaskService;
import sparta.task.service.UploadFileService;

@Slf4j
@Tag(name = "Task Controller", description = "Task 컨트롤러입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/task")
public class TaskController {
    private final TaskService taskService;
    private final UploadFileService uploadFileService;

    @Value("${file.dir}")
    private String fileDir;

    @Operation(summary = "task를 생성합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", content = @Content(schema = @Schema(implementation = TaskResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "validation 오류"),
    })
    @PostMapping
    ResponseEntity<?> createTask(@Valid @RequestBody CreateTaskRequestDto createTaskRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(taskService.createTask(createTaskRequestDto));
    }

    @Operation(summary = "task를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = TaskResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "삭제된 task"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 task"),
    })
    @GetMapping("/{id}")
    ResponseEntity<?> showTaskById(@PathVariable Long id) {
        return ResponseEntity.ok(this.taskService.showTaskById(id));
    }

    @Operation(summary = "모든 task를 조회합니다.")
    @ApiResponse(responseCode = "200", content = @Content(array = @ArraySchema(schema = @Schema(implementation = TaskResponseDto.class))), description = "삭제되지 않은 모든 task를 시간순(desc)으로 정렬합니다.")
    @GetMapping
    ResponseEntity<?> showAllTasks() {
        return ResponseEntity.ok(this.taskService.showAll());
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
            @Valid @RequestBody UpdateTaskRequestDto updateTaskRequestDto) {
        return ResponseEntity.ok(this.taskService.updateTaskBy(id, updateTaskRequestDto));
    }

    @Operation(summary = "task를 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "delete success"),
            @ApiResponse(responseCode = "400", description = "already deleted", content = @Content(schema = @Schema(implementation = CustomErrorResponse.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "password error", content = @Content(schema = @Schema(implementation = CustomErrorResponse.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "not found", content = @Content(schema = @Schema(implementation = CustomErrorResponse.class), mediaType = "application/json"))
    })
    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteTask(@PathVariable Long id,
                                 @Valid @RequestBody DeleteTaskRequestDto deleteTaskRequestDto
    ) {
        this.taskService.deleteBy(id, deleteTaskRequestDto);
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
                                        @Valid @ModelAttribute UploadFileRequestDto uploadFileRequestDto
    ) {
        Task task = this.taskService.findById(id, uploadFileRequestDto.getPassword());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(this.uploadFileService.fileUploadTo(task.getId(), uploadFileRequestDto.getFile()));
    }

    @Operation(summary = "task에 업로드 된 파일을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = @Content(array = @ArraySchema(schema = @Schema(implementation = UploadFileRequestDto.class)))),
            @ApiResponse(responseCode = "404", description = "task not found")
    })
    @GetMapping("/{id}/attachment")
    public ResponseEntity<?> showFiles(@PathVariable Long id) {
        return ResponseEntity.ok(this.taskService.findUploadFilesByTaskId(id));
    }

    // task에 업로드 된 전체 파일 다운로드
    @GetMapping("/{id}/attachment/download")
    public String downloadAllAttachments(@PathVariable Long id) {
        return "";
    }

    // task에 업로드 된 파일 중 일부만 다운로드
    @GetMapping("/{taskId}/attachment/download/{fileId}")
    public String downloadAttachment(@PathVariable Long taskId, @PathVariable Long fileId) {
        return "";
    }
}
