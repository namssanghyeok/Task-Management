package sparta.task.infrastructure.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import sparta.task.domain.model.User;
import sparta.task.domain.repository.UserRepository;
import sparta.task.infrastructure.exception.HttpStatusException;
import sparta.task.infrastructure.exception.constants.ErrorCode;
import sparta.task.infrastructure.repository.jpa.UserJpaRepository;

@RequiredArgsConstructor
@Repository
public class UserRepositoryImpl implements UserRepository {
    private final UserJpaRepository userJpaRepository;

    @Override
    public User getByUsername(String username) {
        // TODO: exception 정의하기
        return userJpaRepository.findByUsername(username)
                .orElseThrow(() -> new HttpStatusException(ErrorCode.USER_NOT_FOUND));
    }

    @Override
    public boolean existsByUsername(String username) {
        return userJpaRepository.existsByUsername(username);
    }

    @Override
    public User save(User user) {
        return userJpaRepository.save(user);
    }
}
