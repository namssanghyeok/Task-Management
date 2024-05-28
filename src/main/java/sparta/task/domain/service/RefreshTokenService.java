package sparta.task.domain.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import sparta.task.application.dto.TokenDto;
import sparta.task.application.dto.request.ReIssueAccessTokenRequestDto;
import sparta.task.domain.model.RefreshToken;
import sparta.task.domain.model.User;
import sparta.task.domain.repository.RefreshTokenRepository;
import sparta.task.infrastructure.exception.HttpStatusException;
import sparta.task.infrastructure.exception.constants.ErrorCode;
import sparta.task.infrastructure.jwt.JwtUtil;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    // create..
    public UUID create(User user) {
        // 기존의 토큰 제거
        refreshTokenRepository.findByUser(user)
                .ifPresent(refreshTokenRepository::delete);
        UUID uuid = UUID.randomUUID();
        refreshTokenRepository.save(
                RefreshToken.builder()
                        .token(uuid)
                        .expiryDate(LocalDateTime.now().plusMonths(1))
                        .user(user)
                        .build()
        );
        return uuid;
    }

    @Transactional
    public String reissueAccessToken(RefreshToken refreshToken) {
        if (refreshToken.expired()) {
            throw new HttpStatusException(ErrorCode.EXPIRED_TOKEN);
        }
        // TODO: refresh token 도 재발급..
        return jwtUtil.createAccessToken(refreshToken.getUser());
    }
}
