package sparta.task.exception.exceptions;

import sparta.task.constants.ErrorCode;

public class ForbiddenException extends HttpStatusException{
    public ForbiddenException() {
        super(ErrorCode.FORBIDDEN);
    }
}
