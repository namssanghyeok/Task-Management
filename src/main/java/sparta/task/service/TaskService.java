package sparta.task.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import sparta.task.dto.CreateTaskDto;
import sparta.task.dto.DeleteTaskDto;
import sparta.task.dto.TaskDto;
import sparta.task.dto.UpdateTaskDto;
import sparta.task.exception.ErrorCode;
import sparta.task.exception.exceptions.HttpStatusException;
import sparta.task.mapper.TaskMapper;
import sparta.task.model.Task;
import sparta.task.repository.TaskRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;

    private final TaskMapper taskMapper;

    public TaskDto createTask(CreateTaskDto createTaskDto) {
        return this.taskMapper.toTaskDto(this.taskRepository.save(
                this.taskMapper.CreateTaskDtoToEntity(createTaskDto)
        ));
    }

    public TaskDto getById(long taskId) {
        Task task = this.findByIdOrThrow(taskId);
        if (task.isDeleted()) {
            throw new HttpStatusException(ErrorCode.ALREADY_DELETED);
        }
        return this.taskMapper.toTaskDto(task);
    }

    public List<TaskDto> showAll() {
        return this.taskRepository.findAllByDeletedAtIsNull(Sort.by(Sort.Direction.DESC, "createdAt"))
                .stream()
                .map(this.taskMapper::toTaskDto)
                .toList();
    }

    public TaskDto updateTaskBy(Long id, UpdateTaskDto updateTaskDto) {
        // 1. find
        Task task = this.taskRepository.findById(id)
                .orElseThrow(() -> new HttpStatusException(ErrorCode.NOT_FOUND));
        // 2. check password. if not, throw 401
        if (task.checkPassword(updateTaskDto.getPassword())) {
            throw new HttpStatusException(ErrorCode.INVALID_PASSWORD);
        }
        // 3. update & save
        task.updateBy(updateTaskDto);
        return this.taskMapper.toTaskDto(this.taskRepository.save(task));
    }

    public void deleteBy(Long id, DeleteTaskDto deleteTaskDto) {
        Task task = this.findByIdOrThrow(id);
        if (task.checkPassword(deleteTaskDto.getPassword())) {
            throw new HttpStatusException(ErrorCode.INVALID_PASSWORD);
        }
        if (task.isDeleted()) {
            throw new HttpStatusException(ErrorCode.ALREADY_DELETED);
        }

        task.delete();
        this.taskRepository.save(task);
    }

    private Task findByIdOrThrow(Long id) {
        return this.taskRepository.findById(id).orElseThrow(() -> new HttpStatusException(ErrorCode.NOT_FOUND));
    }
}
