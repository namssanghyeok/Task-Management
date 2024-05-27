package sparta.task.presentational.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sparta.task.application.dto.request.ReIssueAccessTokenRequestDto;
import sparta.task.application.service.RefreshTokenService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final RefreshTokenService refreshTokenService;


    @PostMapping("/reissue")
    public ResponseEntity<?> reissueAccessToken(@Valid @RequestBody ReIssueAccessTokenRequestDto requestDto) {
        /*
        TODO: refresh token 도 재발급 해야함.
         */
        return ResponseEntity.ok(
                this.refreshTokenService.reissueAccessToken(requestDto)
        );
    }
}
