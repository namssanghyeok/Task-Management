package sparta.task.presentational.web.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;
import sparta.task.presentational.web.validation.annotation.File;

@Getter
@Setter
@Builder
public class UploadFileRequestDto {
    @NotBlank
    @Size(min = 4, max = 30)
    @Schema(example = "password")
    private String password;

    @File
    private MultipartFile file;
}
