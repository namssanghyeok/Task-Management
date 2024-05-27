package sparta.task.presentational.web.exception.handler;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import sparta.task.infrastructure.exception.HttpStatusException;
import sparta.task.presentational.web.exception.CustomErrorResponse;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler
    ResponseEntity<?> handleException(HttpStatusException ex, HttpServletRequest request) {
        return ResponseEntity.status(ex.getErrorCode().getCode()).body(
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
    ResponseEntity<CustomErrorResponse> handleBeanValidationException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        StringBuilder sb = new StringBuilder();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            FieldError fieldError = (FieldError) error;
            sb.append(String.format("%s %s\n", fieldError.getField(), fieldError.getDefaultMessage()));
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
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
