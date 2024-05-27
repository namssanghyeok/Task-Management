package sparta.task.domain.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import sparta.task.domain.model.Task;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskRepository {
    Task save(Task task);

    Optional<Task> findById(Long id);

    Task getById(Long id);

    List<Task> findAllByDeletedAtIsNull(Sort sort);

    /* task with comment */
    Task getTaskWithCommentByTaskIdAndCommentId(Long taskId, UUID commentId);
}
