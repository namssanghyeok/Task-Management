package sparta.task.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class TokenDto {
    private final String accessToken;
    private final String refreshToken;
}
