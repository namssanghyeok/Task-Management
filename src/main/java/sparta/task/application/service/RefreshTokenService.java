package sparta.task.application.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import sparta.task.presentational.dto.TokenDto;
import sparta.task.presentational.dto.request.ReIssueAccessTokenRequestDto;
import sparta.task.infrastructure.exception.constants.ErrorCode;
import sparta.task.infrastructure.exception.HttpStatusException;
import sparta.task.infrastructure.jwt.JwtUtil;
import sparta.task.domain.model.RefreshToken;
import sparta.task.domain.model.User;
import sparta.task.infrastructure.repository.jpa.RefreshTokenJpaRepository;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final JwtUtil jwtUtil;
    private final RefreshTokenJpaRepository refreshTokenJpaRepository;

    // create..
    public UUID create(User user) {
        // 기존의 토큰 제거
        this.refreshTokenJpaRepository.findByUser(user).ifPresent(this.refreshTokenJpaRepository::delete);
        UUID uuid = UUID.randomUUID();
        this.refreshTokenJpaRepository.save(
                RefreshToken.builder()
                        .token(uuid)
                        .expiryDate(LocalDateTime.now().plusMonths(1))
                        .user(user)
                        .build()
        );
        return uuid;
    }

    // getByToken
    public RefreshToken getByToken(UUID token) {
        return this.refreshTokenJpaRepository.findByToken(token)
                .orElseThrow(() -> new HttpStatusException(ErrorCode.NOT_FOUND));
    }

    @Transactional
    public TokenDto reissueAccessToken(ReIssueAccessTokenRequestDto requestDto) {
        RefreshToken refreshToken = this.getByToken(requestDto.getRefreshToken());
        // check refresh token is expired
        if (refreshToken.expired()) {
            throw new HttpStatusException(ErrorCode.EXPIRED_TOKEN);
        }
        // TODO: refresh token 도 재발급..
        String accessToken = jwtUtil.createAccessToken(refreshToken.getUser());
        return TokenDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getToken())
                .build();
    }

    public void deleteExpiredTokens() {
        int BATCH_SIZE = 100;
        LocalDateTime now = LocalDateTime.now();
        PageRequest pageRequest = PageRequest.of(0, BATCH_SIZE);
        Page<RefreshToken> expiredTokens;
        do {
            expiredTokens = this.refreshTokenJpaRepository.findByExpiryDateBefore(now, pageRequest);
            if (!expiredTokens.isEmpty()) {
                this.refreshTokenJpaRepository.deleteAll(expiredTokens.getContent());
                this.refreshTokenJpaRepository.flush();
            }

        } while (!expiredTokens.isEmpty());
    }
}
