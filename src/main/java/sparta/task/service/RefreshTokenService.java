package sparta.task.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sparta.task.dto.TokenDto;
import sparta.task.dto.request.ReIssueAccessTokenRequestDto;
import sparta.task.exception.ErrorCode;
import sparta.task.exception.exceptions.HttpStatusException;
import sparta.task.jwt.JwtUtil;
import sparta.task.model.RefreshToken;
import sparta.task.model.User;
import sparta.task.repository.RefreshTokenRepository;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    // create..
    public UUID create(User user) {
        // 기존의 토큰 제거
        this.refreshTokenRepository.findByUser(user).ifPresent(this.refreshTokenRepository::delete);
        UUID uuid = UUID.randomUUID();
        this.refreshTokenRepository.save(
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
        return this.refreshTokenRepository.findByToken(token)
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

}
