package sparta.task.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import sparta.task.domain.model.common.TimeStamp;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@DynamicInsert
@Table(name = "member")
@Entity
@Getter
public class User extends TimeStamp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    private String nickname;

    private String username;

    private String password;

    @Enumerated(EnumType.ORDINAL)
    private UserRoleEnum role;

    @Transient
    public boolean isAdmin() {
        return this.role.equals(UserRoleEnum.ADMIN);
    }
}
