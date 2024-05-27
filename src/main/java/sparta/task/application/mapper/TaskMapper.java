package sparta.task.application.mapper;

import org.springframework.stereotype.Component;
import sparta.task.presentational.web.dto.request.CreateTaskRequestDto;
import sparta.task.presentational.web.dto.request.UpdateTaskRequestDto;
import sparta.task.presentational.web.dto.response.TaskResponseDto;
import sparta.task.domain.model.Task;
import sparta.task.domain.model.User;

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
                .build();
    }

    public Task updateTaskDtoToEntity(UpdateTaskRequestDto updateTaskRequestDto,
                                      User assignee) {
        return Task.builder()
                .title(updateTaskRequestDto.getTitle())
                .content(updateTaskRequestDto.getContent())
                .assignee(assignee)
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
