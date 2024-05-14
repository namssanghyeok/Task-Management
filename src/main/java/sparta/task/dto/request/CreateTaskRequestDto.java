package sparta.task.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "task 생성 시 사용됩니다.")
@Getter
@Setter
public class CreateTaskRequestDto {
    @Schema(description = "task 제목입니다.", example = "머리 자르기")
    @NotBlank
    @Size(max = 200)
    private String title;

    @Schema(description = "task 내용입니다.", example = "예약")
    private String content;

    @Schema(description = "작업자 형식은 email입니다.", example = "hello@gmail.com")
    @Email
    private String assignee;

    @NotBlank
    @Size(min = 4, max = 30)
    @Schema(example = "password")
    private String password;
}
