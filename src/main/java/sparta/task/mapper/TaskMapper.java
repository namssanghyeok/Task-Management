package sparta.task.mapper;

import org.springframework.stereotype.Component;
import sparta.task.dto.request.CreateTaskRequestDto;
import sparta.task.dto.response.TaskResponseDto;
import sparta.task.model.Task;
import sparta.task.model.User;

import java.time.LocalDateTime;

@Component
public class TaskMapper {
    public Task CreateTaskDtoToEntity(CreateTaskRequestDto createTaskRequestDto,
                                      User currentUser,
                                      User assignee
                                      ) {
        return Task.builder()
                .title(createTaskRequestDto.getTitle())
                .content(createTaskRequestDto.getContent())
                .author(currentUser)
                .assignee(assignee)
                .password(createTaskRequestDto.getPassword())
                .build();
    }

    public TaskResponseDto toTaskDto(Task task) {
        return TaskResponseDto.builder()
                .id(task.getId())
                .title(task.getTitle())
                .content(task.getContent())
                .assignee(task.getAssignee().getUsername())
                .createdAt(task.getCreatedAt())
                .updatedAt(task.getUpdatedAt())
                .build();
    }
}
