package sparta.task.presentational.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SignupUserRequestDto {
    @NotBlank
    @Size(min = 4, max = 10)
    private String username;

    @NotBlank
    @Size(min = 4, max = 30)
    private String nickname;

    @NotBlank
    @Size(min = 8, max = 15)
    private String password;
}
