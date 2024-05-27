package sparta.task.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Schema(description = "task 생성 시 사용됩니다.")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateTaskRequestDto {
    @Schema(description = "task 제목입니다.", example = "머리 자르기")
    @NotBlank
    @Size(max = 200)
    private String title;

    @Schema(description = "task 내용입니다.", example = "예약")
    private String content;

    @Schema(description = "작업자의 username 입니다", example = "username")
    @Size(min = 4, max = 10)
    private String assignee;
}
