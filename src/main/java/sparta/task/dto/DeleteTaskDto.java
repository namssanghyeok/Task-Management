package sparta.task.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeleteTaskDto {
    @NotBlank
    @Size(min = 4, max = 30)
    private String password;
}
