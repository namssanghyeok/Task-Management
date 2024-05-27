package sparta.task.presentational.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequestDto {
    @NotBlank
    @Size(min = 4, max = 10)
    private String username;

    @NotBlank
    @Size(min = 8, max = 15)
    private String password;
}
