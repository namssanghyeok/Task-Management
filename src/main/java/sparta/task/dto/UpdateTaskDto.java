package sparta.task.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateTaskDto {
    @Size(min = 4, max = 50)
    private String title;

    private String content;

    private String assignee;

    @NotBlank
    private String password;
}
