package sparta.task.exception.exceptions;

import sparta.task.exception.ErrorCode;

public class UserNotFound extends HttpStatusException{
    public UserNotFound() {
        super(ErrorCode.USER_NOT_FOUND);
    }
}