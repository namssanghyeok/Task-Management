package sparta.task.mapper;

import org.springframework.stereotype.Component;
import sparta.task.dto.request.CreateCommentRequestDto;
import sparta.task.dto.request.UpdateCommentDto;
import sparta.task.dto.response.CommentResponseDto;
import sparta.task.model.Comment;
import sparta.task.model.Task;
import sparta.task.model.User;

import java.util.UUID;

@Component
public class CommentMapper {
    public Comment createDtoToEntity(CreateCommentRequestDto requestDto, User user) {
        return Comment.builder()
                .id(UUID.randomUUID())
                .content(requestDto.getContent())
                .author(user)
                .build();
    }

    public Comment updateDtoToEntity(UpdateCommentDto requestDto) {
        return Comment.builder()
                .content(requestDto.getContent())
                .build();
    }


    public CommentResponseDto toCommentResponseDto(Comment comment) {
        return CommentResponseDto.builder()
                .commentId(comment.getId())
                .content(comment.getContent())
                .taskId(comment.getTask().getId())
                .userId(comment.getAuthor().getId())
                .build();

    }

}
