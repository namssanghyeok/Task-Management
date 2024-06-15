package sparta.task.domain.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;


@ExtendWith(MockitoExtension.class)
public class CommentTest {
    private Comment comment;
    private User author;

    @BeforeEach
    void init() {
        author = User.builder().id((long) 1).username("username").nickname("nickname").role(UserRoleEnum.USER).build();
        comment = Comment.builder()
                .id(UUID.randomUUID())
                .author(author)
                .build();
    }

    @Test
    @DisplayName("작성자는 수정 할 수 있음")
    void canUpdateTest() {
        // given
        // when
        boolean b = comment.canUpdateBy(author);
        // then
        assertThat(b).isTrue();
    }

    @Test
    @DisplayName("작성자가 아니면 수정 불가능")
    void cantUpdateTest() {
        // given
        User another = mock(User.class);
        given(another.isAdmin())
                .willReturn(false);
        given(another.getId())
                .willReturn((long) 100);

        // when
        boolean b = comment.canUpdateBy(another);

        // then
        assertThat(b).isFalse();
    }

    @Test
    @DisplayName("어드민은 수정 할 수 있음")
    void canUpdateByAdmin() {
        // given
        User admin = mock(User.class);
        given(admin.isAdmin())
                .willReturn(true);
        // when
        boolean b = comment.canUpdateBy(admin);

        // then
        assertThat(b).isTrue();
    }
}
