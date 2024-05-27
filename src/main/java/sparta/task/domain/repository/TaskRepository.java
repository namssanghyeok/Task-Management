package sparta.task.domain.repository;

import org.springframework.data.domain.Sort;
import sparta.task.domain.model.Task;

import java.util.List;
import java.util.UUID;

public interface TaskRepository {
    Task save(Task task);

    Task getById(Long id);

    List<Task> findAllByDeletedAtIsNull(Sort sort);

    /* task with comment */
    Task getTaskWithCommentByTaskIdAndCommentId(Long taskId, UUID commentId);
}
