package sparta.task.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@Builder
public class TokenDto {
    private final String accessToken;
    private final UUID refreshToken;
}
