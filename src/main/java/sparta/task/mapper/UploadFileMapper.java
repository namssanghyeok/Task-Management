package sparta.task.mapper;

import org.springframework.stereotype.Component;
import sparta.task.dto.response.UploadFileResponseDto;
import sparta.task.model.UploadFile;

@Component
public class UploadFileMapper {
    public UploadFileResponseDto toUploadFileResponseDto(UploadFile uploadFile) {
        return UploadFileResponseDto.builder()
                .id(uploadFile.getId())
                .originalFilename(uploadFile.getOriginalFilename())
                .filename(uploadFile.getFilename())
                .size(uploadFile.getSize())
                .type(uploadFile.getType())
                .taskId(uploadFile.getTaskId())
                .build();
    }
}
