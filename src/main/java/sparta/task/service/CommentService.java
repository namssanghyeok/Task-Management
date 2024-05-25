package sparta.task.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sparta.task.dto.request.CreateCommentRequestDto;
import sparta.task.dto.request.UpdateCommentDto;
import sparta.task.dto.response.CommentResponseDto;
import sparta.task.constants.ErrorCode;
import sparta.task.exception.exceptions.HttpStatusException;
import sparta.task.mapper.CommentMapper;
import sparta.task.model.Comment;
import sparta.task.model.Task;
import sparta.task.model.User;
import sparta.task.repository.CommentRepository;

// Task라는 aggregateRoot 에 속하게 됨.
@RequiredArgsConstructor
@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    public CommentResponseDto addCommentToTask(CreateCommentRequestDto createCommentRequestDto, Task task, User currentUser) {
        return this.commentMapper.toCommentResponseDto(this.commentRepository.save(
                this.commentMapper.createDtoToEntity(createCommentRequestDto, task, currentUser)
        ));
    }

    @Transactional
    public void deleteComment(Long commentId, Long taskId, User currentUser) {
        Comment comment = this.getById(commentId);
        // check has authority
        if (!comment.canUpdateBy(currentUser)) {
            throw new HttpStatusException(ErrorCode.INVALID_USER);
        }
        if (!comment.getTask().getId().equals(taskId)) {
            throw new HttpStatusException(ErrorCode.BAD_REQUEST);
        }
        // check comment is already deleted
        if (comment.isDeleted()) {
            throw new HttpStatusException(ErrorCode.ALREADY_DELETED);
        }
        // delete
        comment.delete();
    }

    @Transactional
    public CommentResponseDto update(Long commentId, UpdateCommentDto requestDto, Long taskId, User currentUser) {
        Comment comment = this.getById(commentId);
        // check has authority
        if (!comment.canUpdateBy(currentUser)) {
            throw new HttpStatusException(ErrorCode.INVALID_USER);
        }
        if (!comment.getTask().getId().equals(taskId)) {
            throw new HttpStatusException(ErrorCode.BAD_REQUEST);
        }
        // check comment is already deleted
        if (comment.isDeleted()) {
            throw new HttpStatusException(ErrorCode.ALREADY_DELETED);
        }
        comment.update(this.commentMapper.updateDtoToEntity(requestDto));
        return this.commentMapper.toCommentResponseDto(comment);
    }

    public Comment getById(Long commentId) {
        return this.commentRepository.findById(commentId)
                .orElseThrow(() -> new HttpStatusException(ErrorCode.NOT_FOUND));
    }
}
