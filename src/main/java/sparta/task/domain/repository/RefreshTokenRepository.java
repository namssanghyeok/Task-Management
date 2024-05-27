package sparta.task.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sparta.task.domain.model.RefreshToken;
import sparta.task.domain.model.User;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository {
    Optional<RefreshToken> findByUser(User user);

    void delete(RefreshToken refreshToken);

    RefreshToken save(RefreshToken refreshToken);

    RefreshToken getByToken(UUID refreshToken);

    Page<RefreshToken> findAllExpiredToken(LocalDateTime now, Pageable pageable);

    void deleteAll(Iterable<RefreshToken> refreshTokens);

    void flush();
}
