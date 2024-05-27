package sparta.task.application.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sparta.task.presentational.web.dto.request.SignupUserRequestDto;
import sparta.task.presentational.web.dto.response.UserResponseDto;
import sparta.task.exception.exceptions.UserAlreadyException;
import sparta.task.application.mapper.UserMapper;
import sparta.task.infrastructure.repository.jpa.UserJpaRepository;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserJpaRepository userJpaRepository;
    private final UserMapper userMapper;

    @Transactional
    public UserResponseDto signup(SignupUserRequestDto requestDto) {
        if (this.userJpaRepository.existsByUsername(requestDto.getUsername())) {
            throw new UserAlreadyException();
        }
        return this.userMapper.toUserResponseDto(this.userJpaRepository.save(
                this.userMapper.SignupRequestDtoToEntity(requestDto)
        ));
    }
}
