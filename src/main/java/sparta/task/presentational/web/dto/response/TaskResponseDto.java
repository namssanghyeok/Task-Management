package sparta.task.presentational.web.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class TaskResponseDto {
    private Long id;
    private String title;
    private String content;
    private String assignee;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
