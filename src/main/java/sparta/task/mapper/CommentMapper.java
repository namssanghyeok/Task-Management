package sparta.task.mapper;

import org.springframework.stereotype.Component;
import sparta.task.dto.request.CreateCommentRequestDto;
import sparta.task.dto.response.CommentResponseDto;
import sparta.task.model.Comment;
import sparta.task.model.Task;
import sparta.task.model.User;

@Component
public class CommentMapper {
    public Comment createDtoToEntity(CreateCommentRequestDto requestDto, User author, Task task) {
        return Comment.builder()
                .content(requestDto.getContent())
                .author(author)
                .task(task)
                .build();
    }

    public CommentResponseDto toCommentResponseDto(Comment comment) {
        return CommentResponseDto.builder()
                .content(comment.getContent())
                .taskId(comment.getTask().getId())
                .userId(comment.getAuthor().getId())
                .build();

    }
}
