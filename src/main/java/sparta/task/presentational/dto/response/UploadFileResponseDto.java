package sparta.task.presentational.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class UploadFileResponseDto {
    private Long id;
    private String filename;
    private String originalFilename;
    private Long size;
    private String type;
    private Long taskId;
}
