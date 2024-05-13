package sparta.task.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.http.HttpStatus;
import sparta.task.exception.exceptions.HttpStatusException;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    /* 400 BadRequest: 잘못된 요청 */
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "Bad Request"),
    ALREADY_DELETED(HttpStatus.BAD_REQUEST, "ALREADY DELETED"),

    /*  403 FORBIDDEN: 접근허용안됨*/
    FORBIDDEN(HttpStatus.FORBIDDEN, "Forbidden"),
    INVALID_PASSWORD(HttpStatus.FORBIDDEN, "PASSWORD IS NOT CORRECT"),

    /*  404 NOT FOUND: 접근허용안됨*/
    NOT_FOUND(HttpStatus.NOT_FOUND, "Not Found"),;

    private final HttpStatus code;
    private final String message;

}
