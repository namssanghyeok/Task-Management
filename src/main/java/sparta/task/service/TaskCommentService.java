package sparta.task.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sparta.task.constants.ErrorCode;
import sparta.task.dto.request.CreateCommentRequestDto;
import sparta.task.dto.request.UpdateCommentDto;
import sparta.task.dto.response.CommentResponseDto;
import sparta.task.exception.exceptions.HttpStatusException;
import sparta.task.mapper.CommentMapper;
import sparta.task.model.Comment;
import sparta.task.model.Task;
import sparta.task.model.User;
import sparta.task.repository.TaskRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaskCommentService {
    private final TaskRepository taskRepository;

    private final CommentMapper commentMapper;

    @Transactional
    public CommentResponseDto addCommentToTask(Long taskId, CreateCommentRequestDto requestDto, User currentUser) {
        Task task = this.findByIdOrThrow(taskId);
        Comment comment = this.commentMapper.createDtoToEntity(requestDto, currentUser);
        task.addComment(comment);
        this.taskRepository.save(task);
        return this.commentMapper.toCommentResponseDto(comment);
    }

    @Transactional
    public void deleteCommentFromTask(Long taskId, UUID commentId, User currentUser) {
        Task task = this.taskRepository.findTaskByCommentId(taskId, commentId)
                .orElseThrow(() -> new HttpStatusException(ErrorCode.NOT_FOUND));
        task.deleteComment(commentId, currentUser);
    }

    @Transactional
    public CommentResponseDto updateCommentFromTask(Long taskId, UUID commentId, UpdateCommentDto requestDto, User currentUser) {
        Task task = this.taskRepository.findTaskByCommentId(taskId, commentId)
                .orElseThrow(() -> new HttpStatusException(ErrorCode.NOT_FOUND));
        Comment comment = task.updateComment(commentId, this.commentMapper.updateDtoToEntity(requestDto), currentUser);
        this.taskRepository.save(task);
        return this.commentMapper.toCommentResponseDto(comment);
    }

    private Task findByIdOrThrow(Long id) {
        return this.taskRepository.findById(id).orElseThrow(() -> new HttpStatusException(ErrorCode.TASK_NOT_FOUND));
    }

}
