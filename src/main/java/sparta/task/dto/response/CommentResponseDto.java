package sparta.task.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CommentResponseDto {
    private String content;
    private long taskId;
    private long userId;
}
