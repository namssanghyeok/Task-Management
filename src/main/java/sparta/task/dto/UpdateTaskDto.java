package sparta.task.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "task 수정 시 사용됩니다.")
@Getter
@Setter
public class UpdateTaskDto {
    @Size(max = 200)
    private String title;

    private String content;

    @Email
    private String assignee;

    @NotBlank
    @Size(min = 4, max = 30)
    private String password;
}
