package sparta.task.exception.advice;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import sparta.task.exception.CustomErrorResponse;
import sparta.task.exception.exceptions.HttpStatusException;

import java.time.LocalDateTime;

// TODO: task controller 로 설정
@RestControllerAdvice
public class TaskExceptionAdvice {

    @ExceptionHandler
    ResponseEntity<?> handleException(HttpStatusException ex, HttpServletRequest request) {
        return ResponseEntity.ok(
                CustomErrorResponse.builder()
                        .timestamp(LocalDateTime.now())
                        .error(ex.getErrorCode().getCode().getReasonPhrase())
                        .status(ex.getErrorCode().getCode().value())
                        .message(ex.getErrorCode().getMessage())
                        .path(request.getRequestURI())
                        .build()
        );
    }

    @ExceptionHandler
    ResponseEntity<CustomErrorResponse> handleException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        StringBuilder sb = new StringBuilder();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            FieldError fieldError = (FieldError) error;
            sb.append(String.format("%s %s\n", fieldError.getField(), fieldError.getDefaultMessage()));
        });
        return ResponseEntity.ok(
                CustomErrorResponse.builder()
                        .timestamp(LocalDateTime.now())
                        .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                        .status(HttpStatus.BAD_REQUEST.value())
                        .message(sb.toString())
                        .path(request.getRequestURI())
                        .build()
        );
    }
}
