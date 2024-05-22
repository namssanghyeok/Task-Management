package sparta.task.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sparta.task.dto.SignupUserRequestDto;
import sparta.task.dto.UserResponseDto;
import sparta.task.mapper.UserMapper;
import sparta.task.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional
    public UserResponseDto signup(SignupUserRequestDto requestDto) {
        return this.userMapper.toUserResponseDto(this.userRepository.save(
                this.userMapper.SignupRequestDtoToEntity(requestDto)
        ));
    }
}
