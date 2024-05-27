package sparta.task.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
public class CommentResponseDto {
    private UUID commentId;
    private String content;
    private long taskId;
    private long userId;
}
