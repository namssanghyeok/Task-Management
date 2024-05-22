package sparta.task.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sparta.task.dto.request.SignupUserRequestDto;
import sparta.task.dto.response.UserResponseDto;
import sparta.task.exception.exceptions.UserAlreadyException;
import sparta.task.mapper.UserMapper;
import sparta.task.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional
    public UserResponseDto signup(SignupUserRequestDto requestDto) {
        if (this.userRepository.existsByUsername(requestDto.getUsername())) {
            throw new UserAlreadyException();
        }
        return this.userMapper.toUserResponseDto(this.userRepository.save(
                this.userMapper.SignupRequestDtoToEntity(requestDto)
        ));
    }
}
