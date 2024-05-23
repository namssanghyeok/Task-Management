package sparta.task.security.exception;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import sparta.task.exception.CustomErrorResponse;

import java.io.IOException;
import java.time.LocalDateTime;


// NOTE: Authorization header 가 없으면 이게 아예 실행되지 않음.
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");
        response.getWriter().write(
                CustomErrorResponse.builder()
                        .timestamp(LocalDateTime.now())
                        .path(request.getRequestURI())
                        .status(HttpStatus.FORBIDDEN.value())
                        .error(HttpStatus.FORBIDDEN.getReasonPhrase())
                        .message("hello world~")
                        .build().toString()
        );
    }
}
