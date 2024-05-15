package sparta.task.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import sparta.task.dto.request.CreateTaskRequestDto;
import sparta.task.dto.request.DeleteTaskRequestDto;
import sparta.task.dto.request.UpdateTaskRequestDto;
import sparta.task.dto.response.TaskResponseDto;
import sparta.task.exception.exceptions.HttpStatusException;
import sparta.task.model.Task;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class TaskServiceTest {
    @Autowired
    private TaskService taskService;

    // db에 쌓이는지 확인 -> db에 쌓임. @Transactional 어노테이션을 사용함으로써 db에 쌓이지 않게 만들어야함.
    // 아니면 테스트에 사용하는 또 다른 db 설정을 하던가..
    // 왜 transactional을 사용하면 db에 쌓이지 않는가? 테스트 환경에서 이 어노테이션이 달린 채로 실행이 되면 자동으로 commit이 안되는건가?
    // 테스트 환경에서만 그러는건가?
    @Test
    @DisplayName("생성 성공")
    @Transactional
    void createTask() {
        // given when then
        CreateTaskRequestDto createTaskRequestDto = new CreateTaskRequestDto();
        createTaskRequestDto.setAssignee("assignee");
        createTaskRequestDto.setPassword("1234");
        createTaskRequestDto.setTitle("title");
        createTaskRequestDto.setContent("content");
        TaskResponseDto task = this.taskService.createTask(createTaskRequestDto);
        TaskResponseDto taskResponseDto = this.taskService.showTaskById(task.getId());
        assertEquals(task.getId(), taskResponseDto.getId());
    }

    @Test
    @DisplayName("생성 후 id를 이용해 찾을 수 있어야함")
    @Transactional
    void showTaskById() {
        // given when then
        CreateTaskRequestDto createTaskRequestDto = new CreateTaskRequestDto();
        createTaskRequestDto.setAssignee("assignee");
        createTaskRequestDto.setPassword("1234");
        createTaskRequestDto.setTitle("title");
        createTaskRequestDto.setContent("content");
        TaskResponseDto task = this.taskService.createTask(createTaskRequestDto);
        TaskResponseDto taskResponseDto = this.taskService.showTaskById(task.getId());
        assertEquals(task.getId(), taskResponseDto.getId());
    }

    @Test
    @DisplayName("존재하지 않는 id로 찾으면 404 에러가 발생해야함")
    @Transactional
    void showTaskByIdNotFound() {
        // 이렇게해도 되나..?
        try {
            this.taskService.showTaskById(1000);
        } catch (HttpStatusException e) {
            assertEquals(HttpStatus.NOT_FOUND, e.getErrorCode().getCode());
        }
    }

    @Test
    @DisplayName("이미삭제된 task를 찾으면 400 에러가 발생해야함")
    @Transactional
    void showTaskByAlreadyDeleted() {
        //given
        CreateTaskRequestDto createTaskRequestDto = new CreateTaskRequestDto();
        createTaskRequestDto.setAssignee("assignee");
        createTaskRequestDto.setPassword("1234");
        createTaskRequestDto.setTitle("title");
        createTaskRequestDto.setContent("content");
        TaskResponseDto task = this.taskService.createTask(createTaskRequestDto);
        DeleteTaskRequestDto deleteTaskRequestDto = new DeleteTaskRequestDto();
        deleteTaskRequestDto.setPassword("1234");

        //when
        this.taskService.deleteBy(task.getId(), deleteTaskRequestDto);

        //then
        try {
            this.taskService.showTaskById(task.getId());
        } catch (HttpStatusException e) {
            assertEquals(e.getErrorCode().getCode(), HttpStatus.BAD_REQUEST);
        }
    }


    @Test
    @DisplayName("ID를 이용해 찾고 password로 검증")
    @Transactional
    void findByIdAndCheckPasswordSuccess() {
        CreateTaskRequestDto createTaskRequestDto = new CreateTaskRequestDto();
        createTaskRequestDto.setAssignee("assignee");
        createTaskRequestDto.setPassword("1234");
        createTaskRequestDto.setTitle("title");
        createTaskRequestDto.setContent("content");
        TaskResponseDto taskDto = this.taskService.createTask(createTaskRequestDto);
        //given when then

        try {
            Task task = this.taskService.findByIdAndCheckPassword(taskDto.getId(), "1234");
            assertEquals(task.getId(), taskDto.getId());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("ID를 이용해 찾았지만 password가 틀리면 403에러 발생")
    @Transactional
    void findByIdAndCheckPasswordFailed() {
        CreateTaskRequestDto createTaskRequestDto = new CreateTaskRequestDto();
        createTaskRequestDto.setAssignee("assignee");
        createTaskRequestDto.setPassword("1234");
        createTaskRequestDto.setTitle("title");
        createTaskRequestDto.setContent("content");
        TaskResponseDto taskDto = this.taskService.createTask(createTaskRequestDto);
        //given when then
        try {
            this.taskService.findByIdAndCheckPassword(taskDto.getId(), "asd");
        } catch (HttpStatusException e) {
            assertEquals(e.getErrorCode().getCode(), HttpStatus.FORBIDDEN);
        }
    }

//    @Test
//    @Transactional
//    void showAll() {
//        CreateTaskRequestDto createTaskRequestDto = new CreateTaskRequestDto();
//        createTaskRequestDto.setAssignee("assignee");
//        createTaskRequestDto.setPassword("1234");
//        createTaskRequestDto.setTitle("title");
//        createTaskRequestDto.setContent("content");
//        this.taskService.createTask(createTaskRequestDto);
//        List<TaskResponseDto> taskResponseDtos = this.taskService.showAll();
//        assertEquals(taskResponseDtos.size(), 1);
//    }

    @Test
    @DisplayName("제목 업데이트 성공")
    @Transactional
    void updateTaskBySuccess() {
        CreateTaskRequestDto createTaskRequestDto = new CreateTaskRequestDto();
        createTaskRequestDto.setAssignee("assignee");
        createTaskRequestDto.setPassword("1234");
        createTaskRequestDto.setTitle("title");
        createTaskRequestDto.setContent("content");
        TaskResponseDto taskDto = this.taskService.createTask(createTaskRequestDto);
        UpdateTaskRequestDto updateTaskRequestDto = new UpdateTaskRequestDto();
        updateTaskRequestDto.setTitle("updated");
        updateTaskRequestDto.setPassword("1234");
        TaskResponseDto res = this.taskService.updateTaskBy(taskDto.getId(), updateTaskRequestDto);
        assertEquals(res.getTitle(), "updated");
    }

    @Test
    @DisplayName("패스워드 에러로 인한 업데이트 실패 403 에러 발생")
    @Transactional
    void updateTaskByFailedPassword() {
        CreateTaskRequestDto createTaskRequestDto = new CreateTaskRequestDto();
        createTaskRequestDto.setAssignee("assignee");
        createTaskRequestDto.setPassword("1234");
        createTaskRequestDto.setTitle("title");
        createTaskRequestDto.setContent("content");
        TaskResponseDto taskDto = this.taskService.createTask(createTaskRequestDto);
        UpdateTaskRequestDto updateTaskRequestDto = new UpdateTaskRequestDto();
        updateTaskRequestDto.setTitle("updated");
        updateTaskRequestDto.setPassword("11");
        try {
            this.taskService.updateTaskBy(taskDto.getId(), updateTaskRequestDto);
        } catch (HttpStatusException e) {
            assertEquals(e.getErrorCode().getCode(), HttpStatus.FORBIDDEN);
        }
    }
    // TODO: jacoco 도입
}