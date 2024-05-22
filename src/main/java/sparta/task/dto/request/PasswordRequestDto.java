package sparta.task.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class PasswordRequestDto {
    @NotBlank
    @Size(min = 4, max = 30)
    private String password;

}
