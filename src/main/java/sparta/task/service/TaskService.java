package sparta.task.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sparta.task.dto.CreateTaskDto;
import sparta.task.dto.TaskDto;
import sparta.task.mapper.TaskMapper;
import sparta.task.repository.TaskRepository;

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
}
