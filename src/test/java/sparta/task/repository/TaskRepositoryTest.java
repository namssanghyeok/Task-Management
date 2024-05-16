package sparta.task.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import sparta.task.model.Task;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@DataJpaTest
class TaskRepositoryTest {
    @Autowired
    private TaskRepository taskRepository;

    @Test
    @DisplayName("task 생성 성공")
    public void taskRepository_create_task() {
        // given
        Task task = Task.builder()
                .title("task")
                .password("password")
                .build();

        // when
        Task saved = this.taskRepository.save(task);

        // then
        assertThat(saved).isNotNull();
    }

    @Test
    @DisplayName("삭제 되지 않은 task 리스트")
    public void taskRepository_findAll_not_deleted_task() {

        Task task = Task.builder()
                .title("task")
                .password("password")
                .build();
        Task deletedTask = Task.builder()
                .title("task")
                .password("password")
                .deletedAt(LocalDateTime.now())
                .build();
        this.taskRepository.save(task);
        this.taskRepository.save(deletedTask);
        List<Task> tasks = this.taskRepository.findAllByDeletedAtIsNull(null);
        assertThat(tasks.size()).isEqualTo(1);
    }
}