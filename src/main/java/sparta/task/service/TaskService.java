package sparta.task.service;

import jakarta.persistence.OrderBy;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import sparta.task.dto.CreateTaskDto;
import sparta.task.dto.TaskDto;
import sparta.task.exception.NotFound.NotFoundException;
import sparta.task.mapper.TaskMapper;
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
}
