package sparta.task.domain.repository;

import sparta.task.domain.model.User;

public interface UserRepository {

    User getByUsername(String username);

    boolean existsByUsername(String username);

    User save(User user);
}
