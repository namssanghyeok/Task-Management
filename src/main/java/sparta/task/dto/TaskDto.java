package sparta.task.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class TaskDto {
    private Long id;
    private String title;
    private String content;
    private String assignee;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
