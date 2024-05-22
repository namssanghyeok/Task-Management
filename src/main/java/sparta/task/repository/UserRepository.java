package sparta.task.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sparta.task.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);
}
