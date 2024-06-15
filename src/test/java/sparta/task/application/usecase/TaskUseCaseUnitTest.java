package sparta.task.application.usecase;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sparta.task.application.dto.request.CreateTaskRequestDto;
import sparta.task.application.dto.request.UpdateTaskRequestDto;
import sparta.task.application.dto.response.TaskResponseDto;
import sparta.task.application.mapper.TaskMapper;
import sparta.task.application.mapper.UploadFileMapper;
import sparta.task.domain.model.Task;
import sparta.task.domain.model.User;
import sparta.task.domain.repository.TaskRepository;
import sparta.task.domain.repository.UserRepository;
import sparta.task.infrastructure.exception.HttpStatusException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class TaskUseCaseUnitTest {
    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;
    private TaskUseCase taskUseCase;

    private Task task;
    private User user;

    @BeforeEach
    void init() {
        taskUseCase = new TaskUseCase(taskRepository, userRepository, new TaskMapper(), new UploadFileMapper());
        user = createUser();
        task = createTask(user);
    }

    // repository 는 해당 레이어 유닛테스트와 독립적이어야함
    @Test
    @DisplayName("task 를 생성할 수 있어야함")
    void createTask() {
        // given
        CreateTaskRequestDto requestDto = mock(CreateTaskRequestDto.class);
        given(taskRepository.save(any()))
                .willReturn(task);
        // when
        TaskResponseDto task = taskUseCase.createTask(requestDto, user);

        // then
        assertThat(task.getTitle()).isEqualTo("title");
    }

    @Test
    @DisplayName("task 를 업데이트")
    void updateTask() {
        // given
        User user = User.builder().id((long) 1).build();
        User newAssignee = User.builder().id((long) 2).username("username").build();
        Task task = Task.builder().id((long) 1).title("title").content("content").assignee(user).author(user).build();

        given(taskRepository.getById(anyLong()))
                .willReturn(task);
        given(userRepository.getByUsername(anyString()))
                .willReturn(newAssignee);

        UpdateTaskRequestDto requestDto = UpdateTaskRequestDto.builder().title("updated title").content("updated content").assignee("username").build();

        // when
        TaskResponseDto updatedTask = taskUseCase.updateTask((long) 1, requestDto, user);

        // then
        assertThat(updatedTask.getTitle()).isEqualTo("updated title");
        assertThat(updatedTask.getContent()).isEqualTo("updated content");
        assertThat(updatedTask.getAssignee()).isEqualTo("username");
    }

    @Test
    @DisplayName("권한없는 유저는 수정 불가능")
    void updateTaskByForbidden() {
        // given
        User anotherUser = mock(User.class);
        given(anotherUser.getId())
                .willReturn(100L);
        given(anotherUser.isAdmin())
                .willReturn(false);// when
        // when
        boolean b = task.canUpdateBy(anotherUser);

        // then
        assertThat(b).isFalse();
    }

    @Test
    @DisplayName("삭제 된 task 를 불러오면 예외가 발생해야함")
    void getDeletedTask() {
        // given
        task.delete();
        given(taskRepository.getById(anyLong()))
                .willReturn(task);

        // when & then
        Assertions.assertThrows(HttpStatusException.class, () -> taskUseCase.getById(1L));
    }

    private User createUser() {
        return User.builder()
                .id(1L)
                .nickname("user")
                .username("username")
                .build();
    }

    private Task createTask(User author) {
        return Task.builder()
                .id(1L)
                .author(author)
                .assignee(author)
                .title("title")
                .build();
    }
}
