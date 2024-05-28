package sparta.task.presentational.web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sparta.task.application.dto.TokenDto;
import sparta.task.application.dto.request.LoginRequestDto;
import sparta.task.application.dto.request.ReIssueAccessTokenRequestDto;
import sparta.task.application.usecase.RefreshTokenUseCase;
import sparta.task.domain.model.User;
import sparta.task.infrastructure.jwt.JwtUtil;
import sparta.task.infrastructure.security.principal.UserPrincipal;

import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final JwtUtil jwtUtil;
    private final RefreshTokenUseCase refreshTokenUseCase;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        /*
         * 로그인 실패 시 AuthenticationEntryPoint 실행됨
         */
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequestDto.getUsername(),
                        loginRequestDto.getPassword(),
                        null
                )
        );

        User user = ((UserPrincipal) authentication.getPrincipal()).getUser();
        String accessToken = jwtUtil.createAccessToken(user);
        UUID refreshToken = refreshTokenUseCase.create(user);
        return ResponseEntity.ok(TokenDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build()
        );
    }

    @PostMapping("/reissue")
    public ResponseEntity<?> reissueAccessToken(@Valid @RequestBody ReIssueAccessTokenRequestDto requestDto) {
        return ResponseEntity.ok(
                this.refreshTokenUseCase.reissueAccessToken(requestDto)
        );
    }
}
