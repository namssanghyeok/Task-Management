package sparta.task.presentation.web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sparta.task.application.usecase.TaskCommentUseCase;
import sparta.task.domain.model.User;
import sparta.task.application.dto.request.CreateCommentRequestDto;
import sparta.task.application.dto.request.UpdateCommentDto;
import sparta.task.presentation.web.argumentResolver.annotation.LoginUser;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/task/{taskId}")
public class TaskCommentController {
    private final TaskCommentUseCase taskCommentUseCase;

    @PostMapping("/comment")
    public ResponseEntity<?> newComment(@PathVariable Long taskId,
                                        @Valid @RequestBody CreateCommentRequestDto requestDto,
                                        @LoginUser User currentUser
    ) {
        return ResponseEntity.ok(this.taskCommentUseCase.addCommentToTask(taskId, requestDto, currentUser));
    }

    @DeleteMapping("/comment/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Long taskId,
                                           @PathVariable UUID commentId,
                                           @LoginUser User currentUser
    ) {
        this.taskCommentUseCase.deleteCommentFromTask(taskId, commentId, currentUser);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/comment/{commentId}")
    public ResponseEntity<?> updateComment(@PathVariable Long taskId,
                                           @PathVariable UUID commentId,
                                           @RequestBody UpdateCommentDto requestDto,
                                           @LoginUser User currentUser
    ) {
        return ResponseEntity.ok(this.taskCommentUseCase.updateCommentFromTask(taskId, commentId, requestDto, currentUser));
    }

}
