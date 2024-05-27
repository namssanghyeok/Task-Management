package sparta.task.application.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sparta.task.application.mapper.UserMapper;
import sparta.task.domain.repository.UserRepository;
import sparta.task.infrastructure.exception.HttpStatusException;
import sparta.task.infrastructure.exception.constants.ErrorCode;
import sparta.task.application.dto.request.SignupUserRequestDto;
import sparta.task.application.dto.response.UserResponseDto;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserMapper userMapper;
    private final UserRepository userRepository;

    // TODO: domain 의 service 로 옮겨야하지 않을까?
    //

    /*
     * TODO: domain 의 service 패키지로 이동해야하지 않을까?
     * 왜냐면, 해당 signup 동작은 실제 domain 로직 그 자체이다.
     * 패키지 구조를 변경하면서 application 의 service 와 domain 의 service 의 차이에 대해 생각해봤었는데,
     * application - service 는 단순 오케스트 레이션을 담당
     * domain - service 는 실제 도메인 로직이 담김 -> 도메인만으로 동작하기에는 부자연스러운 동작을 해당 객체에서 진행
     * signup 이라는 동작은 도메인 객체에서 진행할 수 없다.
     */
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
