package sparta.task.infrastructure.repository.jpa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import sparta.task.domain.model.RefreshToken;
import sparta.task.domain.model.User;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenJpaRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(UUID token);

    Optional<RefreshToken> findByUser(User user);

    Page<RefreshToken> findByExpiryDateBefore(LocalDateTime now, Pageable pageable);
}
