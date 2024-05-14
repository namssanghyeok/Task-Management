package sparta.task.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;
import sparta.task.validation.annotation.File;

@Getter
@Setter
public class UploadFileRequestDto {
    @NotBlank
    @Size(min = 4, max = 30)
    @Schema(example = "password")
    private String password;

    @File
    private MultipartFile file;
}
