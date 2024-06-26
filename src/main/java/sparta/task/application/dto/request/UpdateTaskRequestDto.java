package sparta.task.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "task 수정 시 사용됩니다.")
@Getter
@Setter
@Builder
public class UpdateTaskRequestDto {
    @Size(max = 200)
    private String title;

    private String content;

    private String assignee;
}
