package sparta.task.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import sparta.task.dto.CreateTaskDto;
import sparta.task.dto.TaskDto;
import sparta.task.dto.UpdateTaskDto;
import sparta.task.exception.Forbidden.ForbiddenException;
import sparta.task.exception.NotFound.NotFoundException;
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
        return this.taskMapper.toTaskDto(
                this.taskRepository.findById(taskId)
                        .orElseThrow(NotFoundException::new)
        );
    }

    public List<TaskDto> showAll() {
        return this.taskRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"))
                .stream()
                .map(this.taskMapper::toTaskDto)
                .toList();
    }

    public TaskDto updateTaskBy(Long id, UpdateTaskDto updateTaskDto) {
        // 1. find
        Task task = this.taskRepository.findById(id).orElseThrow(NotFoundException::new);
        // 2. check password. if not, throw 401
        if (!task.getPassword().equals(updateTaskDto.getPassword())) {
            throw new ForbiddenException("PASSWORD INCORRECT");
        }
        // 3. update & save
        task.updateBy(updateTaskDto);
        return this.taskMapper.toTaskDto(this.taskRepository.save(task));
    }
}
