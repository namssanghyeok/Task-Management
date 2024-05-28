package sparta.task.application.usecase;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import sparta.task.application.dto.request.LoginRequestDto;
import sparta.task.domain.repository.RefreshTokenRepository;
import sparta.task.application.dto.TokenDto;
import sparta.task.application.dto.request.ReIssueAccessTokenRequestDto;
import sparta.task.domain.service.RefreshTokenService;
import sparta.task.infrastructure.annotation.UseCase;
import sparta.task.infrastructure.exception.constants.ErrorCode;
import sparta.task.infrastructure.exception.HttpStatusException;
import sparta.task.infrastructure.jwt.JwtUtil;
import sparta.task.domain.model.RefreshToken;
import sparta.task.domain.model.User;
import sparta.task.infrastructure.security.principal.UserPrincipal;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@UseCase
@RequiredArgsConstructor
public class RefreshTokenUseCase {
    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;

    public TokenDto login(LoginRequestDto loginRequestDto) {
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
        // NOTE: 여기서 안해도 됨. 매 요청마다 OncePerRequest 필터가 동작하면서 이거 세팅해줌
        // SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = ((UserPrincipal) authentication.getPrincipal()).getUser();
        String accessToken = jwtUtil.createAccessToken(user);
        UUID refreshToken = refreshTokenService.create(user);
        return TokenDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Transactional
    public TokenDto reissueAccessToken(ReIssueAccessTokenRequestDto requestDto) {
        RefreshToken refreshToken = refreshTokenRepository.getByToken(requestDto.getRefreshToken());
        String accessToken = this.refreshTokenService.reissueAccessToken(refreshToken);
        return TokenDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getToken())
                .build();
    }

    @Transactional
    public void deleteExpiredTokens() {
        int BATCH_SIZE = 100;
        LocalDateTime now = LocalDateTime.now();
        PageRequest pageRequest = PageRequest.of(0, BATCH_SIZE);
        Page<RefreshToken> expiredTokens;
        do {
            expiredTokens = refreshTokenRepository.findAllExpiredToken(now, pageRequest);
            if (!expiredTokens.isEmpty()) {
                refreshTokenRepository.deleteAll(expiredTokens.getContent());
                refreshTokenRepository.flush();
            }

        } while (!expiredTokens.isEmpty());
    }
}
