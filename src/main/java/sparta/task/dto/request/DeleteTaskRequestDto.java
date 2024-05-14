package sparta.task.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "task 삭제 시 사용됩니다.")
@Getter
@Setter
public class DeleteTaskRequestDto {
    @NotBlank
    @Size(min = 4, max = 30)
    private String password;
}
