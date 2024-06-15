package sparta.task.infrastructure.repository;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import sparta.task.domain.model.Task;
import sparta.task.domain.model.User;
import sparta.task.domain.model.UserRoleEnum;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {TaskRepositoryImpl.class, UserRepositoryImpl.class}))
public class TaskRepositoryTest {
    @Autowired
    private TaskRepositoryImpl taskRepository;

    @Autowired
    private UserRepositoryImpl userRepository;

    @Test
    @DisplayName("task get 성공")
    @Transactional
    void getByIdTest() {
        // given
        User user = createUser("helloworld", UserRoleEnum.USER);
        User author = userRepository.save(user);

        // when
        Task savedTask = taskRepository.save(Task.builder().title("title").content("content").author(author).assignee(author).build());

        // then
        Task foundTask = taskRepository.getById(savedTask.getId());
        assertThat(savedTask.getId()).isEqualTo(foundTask.getId());
    }

    @Test
    @DisplayName("존재하지 않는 task 찾을 시 예외 발생")
    @Transactional
    void test() {
        // given & when & then
        Assertions.assertThrows(RuntimeException.class, () -> taskRepository.getById(10L));
    }

    // user
    private User createUser(String username, UserRoleEnum role) {
        return User.builder().username(username).nickname(username).role(role).password("password").build();
    }
}
