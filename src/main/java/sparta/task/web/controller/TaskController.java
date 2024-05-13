package sparta.task.web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sparta.task.dto.CreateTaskDto;
import sparta.task.dto.DeleteTaskDto;
import sparta.task.dto.UpdateTaskDto;
import sparta.task.service.TaskService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/task")
public class TaskController {
    private final TaskService taskService;

    @PostMapping
    ResponseEntity<?> createTask(@Valid @RequestBody CreateTaskDto createTaskDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(taskService.createTask(createTaskDto));
    }

    @GetMapping("/{id}")
    ResponseEntity<?> showTaskById(@PathVariable Long id) {
        return ResponseEntity.ok(this.taskService.getById(id));
    }

    @GetMapping
    ResponseEntity<?> showAllTasks() {
        return ResponseEntity.ok(this.taskService.showAll());
    }

    @PutMapping("/{id}")
    ResponseEntity<?> updateTask(
            @PathVariable Long id,
            @Valid @RequestBody UpdateTaskDto updateTaskDto) {
        return ResponseEntity.ok(this.taskService.updateTaskBy(id, updateTaskDto));
    }

    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteTask(@PathVariable Long id,
                                 @Valid @RequestBody DeleteTaskDto deleteTaskDto
    ) {
        this.taskService.deleteBy(id, deleteTaskDto);
        return ResponseEntity.noContent().build();
    }
}
