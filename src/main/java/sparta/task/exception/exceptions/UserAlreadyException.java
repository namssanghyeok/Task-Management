package sparta.task.exception.exceptions;

import sparta.task.constants.ErrorCode;

public class UserAlreadyException extends HttpStatusException{
    public UserAlreadyException() {
        super(ErrorCode.USER_ALREADY_EXISTS);
    }
}
