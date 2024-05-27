package sparta.task.application.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import sparta.task.application.dto.request.SignupUserRequestDto;
import sparta.task.application.dto.response.UserResponseDto;
import sparta.task.domain.model.User;

@Component
@RequiredArgsConstructor
public class UserMapper {
    private final PasswordEncoder passwordEncoder;

    public User SignupRequestDtoToEntity(SignupUserRequestDto requestDto) {
        return User.builder()
                .nickname(requestDto.getNickname())
                .username(requestDto.getUsername())
                .password(passwordEncoder.encode(requestDto.getPassword()))
                .build();
    }
    public UserResponseDto toUserResponseDto(User user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .nickname(user.getNickname())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
