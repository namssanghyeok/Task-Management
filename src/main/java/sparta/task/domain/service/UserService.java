package sparta.task.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sparta.task.domain.model.User;
import sparta.task.domain.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    // check exists
    public boolean checkIfUserExists(User user) {
        return userRepository.existsByUsername(user.getUsername());
    }
}
