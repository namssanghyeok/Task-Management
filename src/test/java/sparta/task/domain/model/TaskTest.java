package sparta.task.domain.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class TaskTest {
    private Task task;
    private User user;
    private User adminUser;
    private User anotherUser;
    private List<Comment> comments;

    @BeforeEach
    void init() {
        comments = new ArrayList<>();
        // comments.add(Comment.builder().build());
        user = User.builder().id((long) 1).nickname("nickname").username("username").build();
        adminUser = User.builder().id((long) 3).nickname("admin").username("admin").role(UserRoleEnum.ADMIN).build();
        anotherUser = User.builder().id((long) 2).nickname("anotherNickname").build();
        task = Task.builder()
                .title("title")
                .content("content")
                .assignee(user)
                .author(user)
                .comments(comments)
                .build();
    }

    @Test
    @DisplayName("task 수정")
    void updateTask() {
        // given
        User assignee = User.builder().id((long) 2).nickname("test").username("test").build();
        Task updateTask = Task.builder()
                .title("updated title")
                .content("updated content")
                .assignee(assignee)
                .build();

        // when
        task.update(updateTask);

        // then
        assertThat(task.getTitle()).isEqualTo("updated title");
        assertThat(task.getContent()).isEqualTo("updated content");
        assertThat(task.getAssignee()).isEqualTo(assignee);
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "updated title"})
    @DisplayName("task title 수정")
    void updateTaskTitle(String title) {
        // given
        Task updateTask = Task.builder()
                .title(title)
                .build();

        // when
        task.update(updateTask);

        // then
        if (title.isEmpty()) {
            assertThat(task.getTitle()).isEqualTo("title");
        } else {
            assertThat(task.getTitle()).isEqualTo("updated title"); }
    }

    @Test
    @DisplayName("author 본인은 수정 가능")
    void canUpdateByTest() {
        // when
        boolean b = task.canUpdateBy(user);
        // then
        assertThat(b).isTrue();
    }

    @Test
    @DisplayName("다른 사람은 수정 불가능")
    void cantUpdateByTest() {
        // given
        User user = Mockito.mock(User.class);
        given(user.getId())
                .willReturn((long) 2);
        // when
        boolean b = task.canUpdateBy(user);
        // then
        assertThat(b).isFalse();
    }

    @Test
    @DisplayName("어드민 Task 수정 가능")
    void canUpdateTaskByAdminTest() {
        // given
        User user = Mockito.mock(User.class);
        given(user.getId())
                .willReturn((long) 2);
        given(user.isAdmin())
                .willReturn(true);
        // when
        boolean b = task.canUpdateBy(user);

        // then
        assertThat(b).isTrue();
    }

    @Test
    @DisplayName("soft 삭제 테스트")
    void isDeletedTest() {
        // given
        // when
        boolean deleted = user.isDeleted();
        // then
        assertThat(deleted).isFalse();
    }

    @Test
    @DisplayName("task 에 comment 추가")
    void addCommentTest(){
        // given
        Comment comment = Mockito.mock(Comment.class);
        // when
        task.addComment(comment);
        // then
        assertThat(task.getComments()).contains(comment);
        assertThat(task.getComments()).size().isEqualTo(1);
    }

    @Test
    @DisplayName("task 에서 comment 삭제")
    void deleteCommentTest() {
        // given
        Comment comment = Mockito.mock(Comment.class);
        UUID commentId = UUID.randomUUID();
        User mockUser = Mockito.mock(User.class);
        given(comment.getId())
                .willReturn(commentId);
        given(comment.canUpdateBy(mockUser))
                .willReturn(true);
        task.addComment(comment);
        // when
        task.deleteComment(commentId,mockUser);
        // then
        assertThat(task.getComments()).doesNotContain(comment);
    }
}
