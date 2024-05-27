package sparta.task.application.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sparta.task.application.mapper.UserMapper;
import sparta.task.domain.repository.UserRepository;
import sparta.task.infrastructure.exception.HttpStatusException;
import sparta.task.infrastructure.exception.constants.ErrorCode;
import sparta.task.presentational.dto.request.SignupUserRequestDto;
import sparta.task.presentational.dto.response.UserResponseDto;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserMapper userMapper;
    private final UserRepository userRepository;

    @Transactional
    public UserResponseDto signup(SignupUserRequestDto requestDto) {
        if (userRepository.existsByUsername(requestDto.getUsername())) {
            throw new HttpStatusException(ErrorCode.USER_ALREADY_EXISTS);
        }
        return userMapper.toUserResponseDto(userRepository.save(
                userMapper.SignupRequestDtoToEntity(requestDto)
        ));
    }
}
