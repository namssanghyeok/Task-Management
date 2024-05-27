package sparta.task.infrastructure.exception.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    /* basic */
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "BAD REQUEST"),
    FORBIDDEN(HttpStatus.FORBIDDEN, "FORBIDDEN"),
    NOT_FOUND(HttpStatus.NOT_FOUND, "NOT FOUND"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL SERVER ERROR"),

    /* 400 BadRequest: 잘못된 요청 */
    ALREADY_DELETED(HttpStatus.BAD_REQUEST, "ALREADY DELETED"),
    INVALID_FILENAME(HttpStatus.BAD_REQUEST, "INVALID FILENAME"),
    EMPTY_FILES(HttpStatus.BAD_REQUEST, "EMPTY FILES"),
    USER_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "USER ALREADY EXISTS"),
    INVALID_TOKEN(HttpStatus.BAD_REQUEST, "INVALID TOKEN"),
    EXPIRED_TOKEN(HttpStatus.BAD_REQUEST, "EXPIRED TOKEN"),

    /*  403 FORBIDDEN: 접근허용안됨*/
    INVALID_PASSWORD(HttpStatus.FORBIDDEN, "PASSWORD IS NOT CORRECT"),
    INVALID_USER(HttpStatus.FORBIDDEN, "ONLY AUTHOR OR ASSIGNEE CAN UPDATE"),

    /*  404 NOT FOUND: 접근허용안됨*/
    TASK_NOT_FOUND(HttpStatus.NOT_FOUND, "TASK NOT FOUND"),
    FILE_NOT_FOUND(HttpStatus.NOT_FOUND, "FILE NOT FOUND"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER NOT FOUND"),

    /* 500 */
    FILE_UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL SERVER ERROR"),
    FILE_DOWNLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL SERVER ERROR");

    private final HttpStatus code;
    private final String message;
}
