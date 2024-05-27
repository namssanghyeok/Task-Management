package sparta.task.presentational.web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sparta.task.presentational.web.dto.request.CreateCommentRequestDto;
import sparta.task.presentational.web.dto.request.UpdateCommentDto;
import sparta.task.domain.model.User;
import sparta.task.presentational.web.argumentResolver.annotation.LoginUser;
import sparta.task.application.service.TaskCommentService;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/task/{taskId}")
public class TaskCommentController {
    private final TaskCommentService taskCommentService;

    @PostMapping("/comment")
    public ResponseEntity<?> newComment(@PathVariable Long taskId,
                                        @Valid @RequestBody CreateCommentRequestDto requestDto,
                                        @LoginUser User currentUser
    ) {
        return ResponseEntity.ok(this.taskCommentService.addCommentToTask(taskId, requestDto, currentUser));
    }

    @DeleteMapping("/comment/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Long taskId,
                                           @PathVariable UUID commentId,
                                           @LoginUser User currentUser
    ) {
        this.taskCommentService.deleteCommentFromTask(taskId, commentId, currentUser);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/comment/{commentId}")
    public ResponseEntity<?> updateComment(@PathVariable Long taskId,
                                           @PathVariable UUID commentId,
                                           @RequestBody UpdateCommentDto requestDto,
                                           @LoginUser User currentUser
    ) {
        return ResponseEntity.ok(this.taskCommentService.updateCommentFromTask(taskId, commentId, requestDto, currentUser));
    }

}
