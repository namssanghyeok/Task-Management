package sparta.task.infrastructure.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import sparta.task.domain.model.RefreshToken;
import sparta.task.domain.model.User;
import sparta.task.domain.repository.RefreshTokenRepository;
import sparta.task.infrastructure.exception.HttpStatusException;
import sparta.task.infrastructure.exception.constants.ErrorCode;
import sparta.task.infrastructure.repository.jpa.RefreshTokenJpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Repository
public class RefreshTokenRepositoryImpl implements RefreshTokenRepository {
    private final RefreshTokenJpaRepository refreshTokenJpaRepository;

    @Override
    public Optional<RefreshToken> findByUser(User user) {
        return this.refreshTokenJpaRepository.findByUser(user);
    }

    @Override
    public void delete(RefreshToken refreshToken) {
        this.refreshTokenJpaRepository.delete(refreshToken);
    }

    @Override
    public RefreshToken save(RefreshToken refreshToken) {
        return this.refreshTokenJpaRepository.save(refreshToken);
    }

    @Override
    public RefreshToken getByToken(UUID refreshToken) {
        return this.refreshTokenJpaRepository.findByToken(refreshToken)
                .orElseThrow(() -> new HttpStatusException(ErrorCode.NOT_FOUND));
    }

    @Override
    public Page<RefreshToken> findAllExpiredToken(LocalDateTime now, Pageable pageable) {
        return this.refreshTokenJpaRepository.findByExpiryDateBefore(now, pageable);
    }

    @Override
    public void deleteAll(Iterable<RefreshToken> refreshTokens) {
        this.refreshTokenJpaRepository.deleteAll(refreshTokens);
    }

    @Override
    public void flush() {
        this.refreshTokenJpaRepository.flush();
    }
}
