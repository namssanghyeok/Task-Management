package sparta.task.web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sparta.task.dto.CreateTaskDto;
import sparta.task.service.TaskService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/task")
public class TaskController {
    private final TaskService taskService;

    @PostMapping
    ResponseEntity<?> createTask(@Valid @RequestBody CreateTaskDto createTaskDto) {
        return ResponseEntity.ok(this.taskService.createTask(createTaskDto));
    }
}
