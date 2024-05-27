package sparta.task.application.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ReIssueAccessTokenRequestDto {
    private UUID refreshToken;
}
