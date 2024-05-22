package sparta.task.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import sparta.task.dto.SignupUserRequestDto;
import sparta.task.dto.UserResponseDto;
import sparta.task.model.User;

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
