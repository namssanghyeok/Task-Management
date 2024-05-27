package sparta.task.infrastructure.security.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import sparta.task.constants.ErrorMessage;
import sparta.task.exception.CustomErrorResponse;
import sparta.task.infrastructure.jwt.JwtUtil;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        boolean expired = JwtUtil.isExpiredAccessToken(request);
        int statusCode = expired ? 499 : HttpStatus.UNAUTHORIZED.value();
        String error = expired ? ErrorMessage.EXPIRED_ACCESS_TOKEN : HttpStatus.UNAUTHORIZED.getReasonPhrase();
        String message = expired ? ErrorMessage.EXPIRED_ACCESS_TOKEN : ErrorMessage.AUTHENTICATION_ERROR;

        response.setStatus(statusCode);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        response.getWriter().write(
                CustomErrorResponse.builder()
                        .timestamp(LocalDateTime.now())
                        .path(request.getRequestURI())
                        .status(statusCode)
                        .error(error)
                        .message(message)
                        .build().toString()
        );
    }
}
