package sparta.task.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sparta.task.model.RefreshToken;

import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(UUID token);
}
