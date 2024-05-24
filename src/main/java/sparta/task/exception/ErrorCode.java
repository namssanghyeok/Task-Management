package sparta.task.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    /* 400 BadRequest: 잘못된 요청 */
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "BAD REQUEST"),
    ALREADY_DELETED(HttpStatus.BAD_REQUEST, "ALREADY DELETED"),
    INVALID_FILENAME(HttpStatus.BAD_REQUEST, "INVALID fILENAME"),
    EMPTY_FILES(HttpStatus.BAD_REQUEST, "EMPTY FILES"),
    USER_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "USER ALREADY EXISTS"),
    INVALID_TOKEN(HttpStatus.BAD_REQUEST, "INVALID TOKEN"),
    EXPIRED_TOKEN(HttpStatus.BAD_REQUEST, "EXPIRED TOKEN"),

    /*  403 FORBIDDEN: 접근허용안됨*/
    FORBIDDEN(HttpStatus.FORBIDDEN, "FORBIDDEN"),
    INVALID_PASSWORD(HttpStatus.FORBIDDEN, "PASSWORD IS NOT CORRECT"),
    INVALID_USER(HttpStatus.FORBIDDEN, "ONLY AUTHOR OR ASSIGNEE CAN UPDATE"),

    /*  404 NOT FOUND: 접근허용안됨*/
    NOT_FOUND(HttpStatus.NOT_FOUND, "NOT FOUND"),
    TASK_NOT_FOUND(HttpStatus.NOT_FOUND, "TASK NOT FOUND"),
    FILE_NOT_FOUND(HttpStatus.NOT_FOUND, "FILE NOT FOUND"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER NOT FOUND"),

    /* 500 */
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL SERVER ERROR"),
    FILE_UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL SERVER ERROR"),
    FILE_DOWNLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL SERVER ERROR");

    private final HttpStatus code;
    private final String message;
}
