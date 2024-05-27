package sparta.task.infrastructure.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import sparta.task.infrastructure.exception.constants.ErrorCode;

@Getter
@RequiredArgsConstructor
public class HttpStatusException extends RuntimeException {
    private final ErrorCode errorCode;
}
