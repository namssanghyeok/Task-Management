package sparta.task.infrastructure.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import sparta.task.domain.model.User;

import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);
}
