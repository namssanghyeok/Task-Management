package sparta.task.exception.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import sparta.task.constants.ErrorCode;

@Getter
@RequiredArgsConstructor
public class HttpStatusException extends RuntimeException {
    private final ErrorCode errorCode;
}
