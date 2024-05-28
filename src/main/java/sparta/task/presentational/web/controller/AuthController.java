package sparta.task.presentational.web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sparta.task.application.dto.request.LoginRequestDto;
import sparta.task.application.dto.request.ReIssueAccessTokenRequestDto;
import sparta.task.application.usecase.RefreshTokenUseCase;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final RefreshTokenUseCase refreshTokenUseCase;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        /*
         * 로그인 실패 시 AuthenticationEntryPoint 실행됨
         */
        return ResponseEntity.ok(this.refreshTokenUseCase.login(loginRequestDto));
    }

    @PostMapping("/reissue")
    public ResponseEntity<?> reissueAccessToken(@Valid @RequestBody ReIssueAccessTokenRequestDto requestDto) {
        return ResponseEntity.ok(
                this.refreshTokenUseCase.reissueAccessToken(requestDto)
        );
    }
}
