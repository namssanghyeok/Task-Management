package sparta.task.application.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sparta.task.application.mapper.UserMapper;
import sparta.task.infrastructure.exception.HttpStatusException;
import sparta.task.infrastructure.exception.constants.ErrorCode;
import sparta.task.infrastructure.repository.jpa.UserJpaRepository;
import sparta.task.presentational.dto.request.SignupUserRequestDto;
import sparta.task.presentational.dto.response.UserResponseDto;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserJpaRepository userJpaRepository;
    private final UserMapper userMapper;

    @Transactional
    public UserResponseDto signup(SignupUserRequestDto requestDto) {
        if (this.userJpaRepository.existsByUsername(requestDto.getUsername())) {
            throw new HttpStatusException(ErrorCode.USER_ALREADY_EXISTS);
        }
        return this.userMapper.toUserResponseDto(this.userJpaRepository.save(
                this.userMapper.SignupRequestDtoToEntity(requestDto)
        ));
    }
}
