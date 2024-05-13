package sparta.task.mapper;

import org.springframework.stereotype.Component;
import sparta.task.dto.CreateTaskDto;
import sparta.task.dto.TaskDto;
import sparta.task.model.Task;

import java.time.LocalDateTime;

@Component
public class TaskMapper {
    public Task CreateTaskDtoToEntity(CreateTaskDto createTaskDto) {
        return Task.builder()
                .title(createTaskDto.getTitle())
                .content(createTaskDto.getContent())
                .assignee(createTaskDto.getAssignee())
                .password(createTaskDto.getPassword())
                .createdAt(LocalDateTime.now())
                .build();
    }

    public TaskDto toTaskDto(Task task) {
        return TaskDto.builder()
                .id(task.getId())
                .title(task.getTitle())
                .content(task.getContent())
                .assignee(task.getAssignee())
                .createdAt(task.getCreatedAt())
                .build();
    }
}
