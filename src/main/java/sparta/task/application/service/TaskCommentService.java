package sparta.task.application.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sparta.task.domain.repository.TaskRepository;
import sparta.task.infrastructure.exception.constants.ErrorCode;
import sparta.task.presentational.dto.request.CreateCommentRequestDto;
import sparta.task.presentational.dto.request.UpdateCommentDto;
import sparta.task.presentational.dto.response.CommentResponseDto;
import sparta.task.infrastructure.exception.HttpStatusException;
import sparta.task.application.mapper.CommentMapper;
import sparta.task.domain.model.Comment;
import sparta.task.domain.model.Task;
import sparta.task.domain.model.User;
import sparta.task.infrastructure.repository.jpa.TaskJpaRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaskCommentService {
    private final TaskRepository taskRepository;
    private final CommentMapper commentMapper;

    @Transactional
    public CommentResponseDto addCommentToTask(Long taskId, CreateCommentRequestDto requestDto, User currentUser) {
        Task task = findByIdOrThrow(taskId);
        Comment comment = commentMapper.createDtoToEntity(requestDto, currentUser);
        task.addComment(comment);
        taskRepository.save(task);
        return commentMapper.toCommentResponseDto(comment);
    }

    @Transactional
    public void deleteCommentFromTask(Long taskId, UUID commentId, User currentUser) {
        Task task = taskRepository.getById(taskId);
        task.deleteComment(commentId, currentUser);
    }

    @Transactional
    public CommentResponseDto updateCommentFromTask(Long taskId, UUID commentId, UpdateCommentDto requestDto, User currentUser) {
        Task task = taskRepository.getById(taskId);
        Comment comment = task.updateComment(commentId, commentMapper.updateDtoToEntity(requestDto), currentUser);
        taskRepository.save(task);
        return commentMapper.toCommentResponseDto(comment);
    }

    private Task findByIdOrThrow(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new HttpStatusException(ErrorCode.TASK_NOT_FOUND));
    }

}
