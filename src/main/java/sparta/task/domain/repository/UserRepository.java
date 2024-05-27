package sparta.task.domain.repository;

import sparta.task.domain.model.User;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findByUsername(String username);

    User getByUsername(String username);

    boolean existsByUsername(String username);

    User save(User user);
}
