package sparta.task.application.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sparta.task.domain.repository.TaskRepository;
import sparta.task.infrastructure.exception.constants.ErrorCode;
import sparta.task.application.dto.request.CreateCommentRequestDto;
import sparta.task.application.dto.request.UpdateCommentDto;
import sparta.task.application.dto.response.CommentResponseDto;
import sparta.task.infrastructure.exception.HttpStatusException;
import sparta.task.application.mapper.CommentMapper;
import sparta.task.domain.model.Comment;
import sparta.task.domain.model.Task;
import sparta.task.domain.model.User;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaskCommentService {
    private final TaskRepository taskRepository;
    private final CommentMapper commentMapper;

    @Transactional
    public CommentResponseDto addCommentToTask(Long taskId, CreateCommentRequestDto requestDto, User currentUser) {
        Task task = taskRepository.getById(taskId);
        Comment comment = commentMapper.createDtoToEntity(requestDto, currentUser);
        task.addComment(comment);
        taskRepository.save(task);
        return commentMapper.toCommentResponseDto(comment);
    }

    @Transactional
    public void deleteCommentFromTask(Long taskId, UUID commentId, User currentUser) {
        Task task = taskRepository.getTaskWithCommentByTaskIdAndCommentId(taskId, commentId);
        task.deleteComment(commentId, currentUser);
    }

    @Transactional
    public CommentResponseDto updateCommentFromTask(Long taskId, UUID commentId, UpdateCommentDto requestDto, User currentUser) {
        Task task = taskRepository.getTaskWithCommentByTaskIdAndCommentId(taskId, commentId);
        Comment comment = task.updateComment(commentId, commentMapper.updateDtoToEntity(requestDto), currentUser);
        taskRepository.save(task);
        return commentMapper.toCommentResponseDto(comment);
    }
}
