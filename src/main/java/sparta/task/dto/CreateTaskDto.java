package sparta.task.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateTaskDto {
    @NotBlank
    @Size(min = 4, max = 50)
    private String title;

    private String content;

    private String assignee;

    @NotBlank
    @Size(min = 4, max = 30)
    private String password;
}
