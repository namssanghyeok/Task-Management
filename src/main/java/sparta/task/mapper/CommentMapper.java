package sparta.task.mapper;

import org.springframework.stereotype.Component;
import sparta.task.dto.request.CreateCommentRequestDto;
import sparta.task.dto.request.UpdateCommentDto;
import sparta.task.dto.response.CommentResponseDto;
import sparta.task.model.Comment;
import sparta.task.model.Task;
import sparta.task.model.User;

@Component
public class CommentMapper {
    public Comment createDtoToEntity(CreateCommentRequestDto requestDto, Task task, User user) {
        return Comment.builder()
                .content(requestDto.getContent())
                .author(user)
                .task(task)
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
