package sparta.task.exception.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import sparta.task.exception.ErrorCode;

@Getter
@RequiredArgsConstructor
public class HttpStatusException extends RuntimeException {
    private final ErrorCode errorCode;
}
