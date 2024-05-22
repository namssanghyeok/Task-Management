package sparta.task.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import sparta.task.dto.request.CreateTaskRequestDto;
import sparta.task.dto.request.DeleteTaskRequestDto;
import sparta.task.dto.request.PasswordRequestDto;
import sparta.task.dto.request.UpdateTaskRequestDto;
import sparta.task.dto.response.TaskResponseDto;
import sparta.task.exception.exceptions.HttpStatusException;
import sparta.task.mapper.TaskMapper;
import sparta.task.mapper.UploadFileMapper;
import sparta.task.model.Task;
import sparta.task.model.UploadFile;
import sparta.task.model.User;
import sparta.task.repository.TaskRepository;
import sparta.task.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private TaskRepository taskRepository;
    @Mock
    private TaskMapper taskMapper;
    @Mock
    private UploadFileMapper uploadFileMapper;

    @InjectMocks
    private TaskService taskService;

    private Task task;
    private TaskResponseDto taskResponseDto;
    private CreateTaskRequestDto createTaskRequestDto;
    private DeleteTaskRequestDto deleteTaskRequestDto;
    private UpdateTaskRequestDto updateTaskRequestDto;

    @BeforeEach
    public void beforeEach() {
        createTaskRequestDto = CreateTaskRequestDto.builder().title("title").content("content").password("1234").build();
        task = Task.builder().id((long) 1).title("title").content("content").password("1234").build();
        taskResponseDto = TaskResponseDto.builder().id((long) 1).title("title").content("content").build();
        deleteTaskRequestDto = DeleteTaskRequestDto.builder().password("1234").build();
        updateTaskRequestDto = UpdateTaskRequestDto.builder().title("hello").password("1234").build();
    }

    @Test
    @DisplayName("task 생성")
    void createTask_success() {
        // given
        // 가상으로 repository 의 save가 실행되면 task가 return되게 한다.
        Mockito.when(this.taskRepository.save(Mockito.any(Task.class)))
                .thenReturn(task);
        // 가상으로 toTaskDto 가 실행되면 TaskResponseDto가 반환되도록 한다.
        Mockito.when(this.taskMapper.toTaskDto(Mockito.any(Task.class)))
                .thenReturn(taskResponseDto);
        Mockito.when(this.userRepository.findByUsername(Mockito.anyString()))
                .thenReturn(Optional.of(Mockito.any(User.class)));
        // 가상으로 createTaskDtoToEntity 가 실행 되면 createTaskDto가 반환되도록한다.
        Mockito.when(this.taskMapper.CreateTaskDtoToEntity(Mockito.any(CreateTaskRequestDto.class), Mockito.any(User.class)))
                .thenReturn(task);

        // when
        TaskResponseDto res = this.taskService.createTask(createTaskRequestDto, userPrincipal.getUser());

        // then
        assertThat(res.getId()).isEqualTo(task.getId());
    }

    @Test
    @DisplayName("생성 후에는 ID를 이용해 찾을 수 있어야함")
    void taskService_showById() {
        // given
        Optional<Task> taskOptional = Optional.of(task);
        Mockito.when(this.taskRepository.findById(Mockito.any(Long.class))).thenReturn(taskOptional);
        Mockito.when(this.taskMapper.toTaskDto(Mockito.any(Task.class))).thenReturn(taskResponseDto);

        // when
        TaskResponseDto res = this.taskService.showTaskById(1);

        // then
        assertThat(res).isEqualTo(taskResponseDto);
    }

    @Test
    @DisplayName("존재하지 않는 ID를 이용해 찾으면 404 예외가 발생함")
    void taskService_showBy_NotFound() {
        // given
        Mockito.when(this.taskRepository.findById(Mockito.any(Long.class))).thenThrow(HttpStatusException.class);

        // when & then
        assertThrows(HttpStatusException.class, () -> this.taskService.showTaskById(1));
    }

    @Test
    @DisplayName("삭제 된 Task를 찾으면 400 예외가 발생")
    void showById_alreadyDeleted() {
        // given
        Mockito.when(this.taskRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(task));
        task.delete();

        try {
            // when
            this.taskService.showTaskById(1L);
        } catch (HttpStatusException e) {
            // then
            assertThat(e.getErrorCode().getCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }
    }

    @Test
    @DisplayName("ID로 찾기 성공")
    void findById_success() {
        // given
        Mockito.when(this.taskRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(task));
        // when
        Task foundTask = this.taskService.findById(1L);
        // then
        assertThat(foundTask.getId()).isEqualTo(task.getId());
    }

    @Test
    @DisplayName("PASSWORD 일치하면 성공")
    void findTaskById_success() {
        // given
        Mockito.when(this.taskRepository.findById(Mockito.any(Long.class)))
                .thenReturn(Optional.of(task));

        // when
        Task foundTask = this.taskService.findByIdAndCheckPassword(1, "1234");

        // then
        assertThat(foundTask).isNotNull();
    }

    @Test
    @DisplayName("PASSWORD가 일치하지 않으면 예외발생")
    void findByIdAndCheckPassword_failed() {
        // given
        Mockito.when(this.taskRepository.findById(Mockito.any(Long.class)))
                .thenReturn(Optional.of(task));
        // when & then
        assertThrows(HttpStatusException.class, () -> this.taskService.findByIdAndCheckPassword(1, "12345"));
    }

    // TODO: 삭제 된 task는 목록에 나오지 않는 작업은 레포지토리 테스트에서 해야함
    @Test
    void showAll_success() {
        // given
        List<Task> tasks = new ArrayList<>();
        tasks.add(task);

        Mockito.when(this.taskRepository.findAllByDeletedAtIsNull(Sort.by(Sort.Direction.DESC, "createdAt")))
                .thenReturn(tasks);

        Mockito.when(this.taskMapper.toTaskDto(Mockito.any(Task.class)))
                .thenReturn(taskResponseDto);

        // when
        List<TaskResponseDto> taskResponseDtos = this.taskService.showAll();
        // then
        assertThat(taskResponseDtos).hasSize(1);
    }

    @Test
    @DisplayName("업데이트 성공")
    void updateTaskBy_success() {
        // given
        Mockito.when(this.taskRepository.findById(Mockito.any(Long.class)))
                .thenReturn(Optional.of(task));

        TaskResponseDto updatedResponseDto = TaskResponseDto.builder().id(1L).title("hello").build();
        Mockito.when(this.taskMapper.toTaskDto(Mockito.any(Task.class)))
                .thenReturn(updatedResponseDto);

        Task updatedTask = Task.builder().id(1L).title("hello").build();
        Mockito.when(this.taskRepository.save(Mockito.any(Task.class))).thenReturn(updatedTask);

        // when
        TaskResponseDto updated = this.taskService.updateTaskBy(1L, updateTaskRequestDto);

        // then
        assertThat(updated.getTitle()).isEqualTo(task.getTitle());
    }

    @Test
    @DisplayName("잘못된 PASSWORD로 업데이트 시 403 예외 발생")
    void updateTaskBy_wrongPassword() {
        // given
        Mockito.when(this.taskRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(task));
        UpdateTaskRequestDto wrongPasswordDto = UpdateTaskRequestDto.builder().password("12345").build();
        try {
            // when
            this.taskService.updateTaskBy(1L, wrongPasswordDto);
        } catch (HttpStatusException e) {
            // then
            assertThat(e.getErrorCode().getCode()).isEqualTo(HttpStatus.FORBIDDEN);
        }
    }

    /**
     * task 자체도 mocking 하고, 결과가 아닌 특정 메서드가 실행되었는지 여부로 테스트의 성공 여부 판별
     */
    @Test
    @DisplayName("task 삭제 - 성공")
    void deleteTaskBy_success() {
        // given
        Task mockTask = Mockito.mock(Task.class);

        Mockito.when(this.taskRepository.findById(Mockito.any(Long.class)))
                .thenReturn(Optional.of(mockTask));
        Mockito.when(mockTask.checkPassword("1234")).thenReturn(false);
        Mockito.when(mockTask.isDeleted()).thenReturn(false);
        // when
        this.taskService.deleteBy(1L, PasswordRequestDto.builder().password("1234").build());
        // then - 아래의 메서드들이 실행 되었는지 확인
        Mockito.verify(mockTask).isDeleted();
        Mockito.verify(this.taskRepository).save(mockTask);

        // 실행된 횟수도 지정할 수 있음
        // Mockito.verify(task, Mockito.times(1)).isDeleted();
        // Mockito.verify(this.taskRepository, Mockito.times(1)).save(task);
    }

    @Test
    @DisplayName("task 삭제 - 잘못된 비밀번호로 삭제시 403 예외")
    void deleteTaskBy_wrongPassword() {
        // given
        Mockito.when(this.taskRepository.findById(Mockito.any(Long.class)))
                .thenReturn(Optional.of(task));
        try {
            // when
            assertThrows(HttpStatusException.class, () -> this.taskService.deleteBy(1L, PasswordRequestDto.builder().password("12345").build()));
            this.taskService.deleteBy(1L, PasswordRequestDto.builder().password("12345").build());
        } catch (HttpStatusException e) {
            // then
            assertThat(e.getErrorCode().getCode()).isEqualTo(HttpStatus.FORBIDDEN);
        }
    }

    @Test
    @DisplayName("task 삭제 - 존재하지 않는 task 삭제시 404 예외")
    void deleteTaskBy_notFound() {
        // given
        // when
        // then
        try {
            assertThrows(HttpStatusException.class, () -> this.taskService.deleteBy(10L, PasswordRequestDto.builder().password("1234").build()));
            this.taskService.deleteBy(10L, PasswordRequestDto.builder().password("1234").build());
        } catch (HttpStatusException e) {
            assertThat(e.getErrorCode().getCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }
    }

    @Test
    @DisplayName("task 삭제 - 이미 삭제된 task 삭제 시도시 400 예외")
    void deleteTaskBy_taskAlreadyDeleted() {
        // given
        Task mockTask = Mockito.mock(Task.class);
        Mockito.when(this.taskRepository.findById(Mockito.any(Long.class)))
                .thenReturn(Optional.of(mockTask));
        Mockito.when(mockTask.isDeleted()).thenReturn(true);
        try {
            // when
            assertThrows(HttpStatusException.class, () -> this.taskService.deleteBy(1L, PasswordRequestDto.builder().password("1234").build()));
            this.taskService.deleteBy(1L, PasswordRequestDto.builder().password("1234").build());
        } catch (HttpStatusException e) {
            // then
            assertThat(e.getErrorCode().getCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }
    }

    @Test
    @DisplayName("task 에 업로드 된 파일 가져오기")
    void findTaskBy_success() {
        // given
        List<UploadFile> files = new ArrayList<>();
        Task mockTask = Mockito.mock(Task.class);
        Mockito.when(this.taskRepository.findById(Mockito.any(Long.class)))
                .thenReturn(Optional.of(mockTask));
        // when
        this.taskService.findUploadFilesByTaskId(1L);
        // then
        Mockito.verify(mockTask).getAttachments();
    }
}
