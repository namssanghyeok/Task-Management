package sparta.task.application.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import sparta.task.presentational.web.dto.request.CreateTaskRequestDto;
import sparta.task.presentational.web.dto.request.UpdateTaskRequestDto;
import sparta.task.presentational.web.dto.response.TaskResponseDto;
import sparta.task.presentational.web.dto.response.UploadFileResponseDto;
import sparta.task.application.mapper.TaskMapper;
import sparta.task.application.mapper.UploadFileMapper;
import sparta.task.constants.ErrorCode;
import sparta.task.domain.model.Task;
import sparta.task.domain.model.User;
import sparta.task.domain.repository.TaskRepository;
import sparta.task.domain.repository.UserRepository;
import sparta.task.exception.exceptions.ForbiddenException;
import sparta.task.exception.exceptions.HttpStatusException;
import sparta.task.exception.exceptions.UserNotFound;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    private final TaskMapper taskMapper;
    private final UploadFileMapper uploadFileMapper;

    public TaskResponseDto createTask(CreateTaskRequestDto createTaskRequestDto, User currentuser) {
        User assignee = this.userRepository.getByUsername(createTaskRequestDto.getAssignee());
        return this.taskMapper.toTaskDto(this.taskRepository.save(
                this.taskMapper.CreateTaskDtoToEntity(createTaskRequestDto, currentuser, assignee)
        ));
    }

    public TaskResponseDto showTaskById(long taskId) {
        Task task = this.taskRepository.getById(taskId);
        if (task.isDeleted()) {
            throw new HttpStatusException(ErrorCode.ALREADY_DELETED);
        }
        return this.taskMapper.toTaskDto(task);
    }

    public Task findById(long taskId) {
        return this.taskRepository.getById(taskId);
    }

    public List<TaskResponseDto> showAll() {
        return this.taskRepository.findAllByDeletedAtIsNull(Sort.by(Sort.Direction.DESC, "createdAt"))
                .stream()
                .map(this.taskMapper::toTaskDto)
                .toList();
    }

    @Transactional
    public TaskResponseDto updateTaskBy(Long id, UpdateTaskRequestDto updateTaskRequestDto, User currentUser) {
        // 1. find
        Task task = this.taskRepository.getById(id);
        // 2. 수정할 수 있는 사람인지?
        if (!task.canUpdateBy(currentUser)) {
            throw new ForbiddenException();
        }
        User newAssignee = null;
        if (updateTaskRequestDto.getAssignee() != null) {
            newAssignee = this.userRepository.findByUsername(updateTaskRequestDto.getAssignee())
                    .orElseThrow(UserNotFound::new);
        }
        // 3. update & save
        task.update(this.taskMapper.updateTaskDtoToEntity(updateTaskRequestDto, newAssignee));
        return this.taskMapper.toTaskDto(task);
    }

    public List<UploadFileResponseDto> findUploadFilesByTaskId(long id) {
        Task task = this.taskRepository.getById(id);
        return task.getAttachments().stream()
                .map(this.uploadFileMapper::toUploadFileResponseDto)
                .toList();
    }

    public void deleteBy(Long id, User currentUser) {
        Task task = this.taskRepository.getById(id);
        task.delete();
        task.delete();
        this.taskRepository.save(task);
    }
}
