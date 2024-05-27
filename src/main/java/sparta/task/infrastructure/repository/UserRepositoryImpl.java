package sparta.task.infrastructure.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import sparta.task.domain.model.User;
import sparta.task.domain.repository.UserRepository;
import sparta.task.infrastructure.repository.jpa.UserJpaRepository;

import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class UserRepositoryImpl implements UserRepository {
    private final UserJpaRepository userJpaRepository;

    @Override
    public Optional<User> findByUsername(String username) {
        return this.userJpaRepository.findByUsername(username);
    }

    @Override
    public User getByUsername(String username) {
        // TODO: exception 정의하기
        return this.userJpaRepository.findByUsername(username)
                .orElseThrow();
    }
}
