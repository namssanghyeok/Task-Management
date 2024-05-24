package sparta.task.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "refresh_token_id")
    private Long id;

    private UUID token;

    private LocalDateTime expiryDate;

    // user 와 1:1. 한명의 유저는 하나의 refresh token만 가지고 있을 수 있음.
    // 새로 만들었는데, 기존에 refresh 토큰이 있다면 delete 해야함
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    // domain logic
    public boolean expired() {
        return LocalDateTime.now().isAfter(this.expiryDate);
    }
}
