package sparta.task.application.usecase;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import sparta.task.application.dto.request.SignupUserRequestDto;
import sparta.task.application.dto.response.UserResponseDto;
import sparta.task.application.mapper.UserMapper;
import sparta.task.domain.model.User;
import sparta.task.domain.repository.UserRepository;
import sparta.task.domain.service.UserService;
import sparta.task.infrastructure.annotation.UseCase;
import sparta.task.infrastructure.exception.HttpStatusException;
import sparta.task.infrastructure.exception.constants.ErrorCode;

@UseCase
@RequiredArgsConstructor
public class UserUseCase {
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final UserService userService;

    @Transactional
    public UserResponseDto signup(SignupUserRequestDto requestDto) {
        User user = userMapper.SignupRequestDtoToEntity(requestDto);
        if (userService.checkIfUserExists(user)) {
            throw new HttpStatusException(ErrorCode.USER_ALREADY_EXISTS);
        }
        return userMapper.toUserResponseDto(userRepository.save(user));
    }
}
