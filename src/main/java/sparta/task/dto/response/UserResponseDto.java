package sparta.task.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class UserResponseDto {
    private long id;
    private String username;
    private String nickname;
    private LocalDateTime createdAt;
}
