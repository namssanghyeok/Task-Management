package sparta.task.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import sparta.task.dto.request.CreateTaskRequestDto;
import sparta.task.dto.request.DeleteTaskRequestDto;
import sparta.task.dto.request.PasswordRequestDto;
import sparta.task.dto.request.UpdateTaskRequestDto;
import sparta.task.dto.response.TaskResponseDto;
import sparta.task.dto.response.UploadFileResponseDto;
import sparta.task.exception.ErrorCode;
import sparta.task.exception.exceptions.HttpStatusException;
import sparta.task.mapper.TaskMapper;
import sparta.task.mapper.UploadFileMapper;
import sparta.task.model.Task;
import sparta.task.repository.TaskRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;

    private final TaskMapper taskMapper;
    private final UploadFileMapper uploadFileMapper;

    public TaskResponseDto createTask(CreateTaskRequestDto createTaskRequestDto) {
        return this.taskMapper.toTaskDto(this.taskRepository.save(
                this.taskMapper.CreateTaskDtoToEntity(createTaskRequestDto)
        ));
    }

    public TaskResponseDto showTaskById(long taskId) {
        Task task = this.findByIdOrThrow(taskId);
        if (task.isDeleted()) {
            throw new HttpStatusException(ErrorCode.ALREADY_DELETED);
        }
        return this.taskMapper.toTaskDto(task);
    }

    public Task findById(long taskId) {
        return this.findByIdOrThrow(taskId);
    }


    public Task findByIdAndCheckPassword(long taskId, String password) {
        Task task = this.findByIdOrThrow(taskId);
        if (task.checkPassword(password)) {
            throw new HttpStatusException(ErrorCode.INVALID_PASSWORD);
        }
        return task;
    }


    public List<TaskResponseDto> showAll() {
        return this.taskRepository.findAllByDeletedAtIsNull(Sort.by(Sort.Direction.DESC, "createdAt"))
                .stream()
                .map(this.taskMapper::toTaskDto)
                .toList();
    }

    public TaskResponseDto updateTaskBy(Long id, UpdateTaskRequestDto updateTaskRequestDto) {
        // 1. find
        Task task = this.findByIdOrThrow(id);
        // 2. check password. if not, throw 401
        if (task.checkPassword(updateTaskRequestDto.getPassword())) {
            throw new HttpStatusException(ErrorCode.INVALID_PASSWORD);
        }
        // 3. update & save
        task.updateBy(updateTaskRequestDto);
        return this.taskMapper.toTaskDto(this.taskRepository.save(task));
    }

    public List<UploadFileResponseDto> findUploadFilesByTaskId(long id) {
        Task task = this.findByIdOrThrow(id);
        return task.getAttachments().stream()
                .map(this.uploadFileMapper::toUploadFileResponseDto)
                .toList();
    }

    public void deleteBy(Long id, PasswordRequestDto deleteTaskRequestDto) {
        Task task = this.findByIdOrThrow(id);
        if (task.checkPassword(deleteTaskRequestDto.getPassword())) {
            throw new HttpStatusException(ErrorCode.INVALID_PASSWORD);
        }
        if (task.isDeleted()) {
            throw new HttpStatusException(ErrorCode.ALREADY_DELETED);
        }
        task.delete();
        this.taskRepository.save(task);
    }

    private Task findByIdOrThrow(Long id) {
        return this.taskRepository.findById(id).orElseThrow(() -> new HttpStatusException(ErrorCode.TASK_NOT_FOUND));
    }
}
