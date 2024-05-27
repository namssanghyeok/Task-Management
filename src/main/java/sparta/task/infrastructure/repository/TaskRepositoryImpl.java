package sparta.task.infrastructure.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import sparta.task.domain.model.Task;
import sparta.task.domain.repository.TaskRepository;
import sparta.task.infrastructure.exception.HttpStatusException;
import sparta.task.infrastructure.exception.constants.ErrorCode;
import sparta.task.infrastructure.repository.jpa.TaskJpaRepository;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Repository
public class TaskRepositoryImpl implements TaskRepository {
    private final TaskJpaRepository taskJpaRepository;

    @Override
    public Task save(Task task) {
        return this.taskJpaRepository.save(task);
    }

    @Override
    public List<Task> findAllByDeletedAtIsNull(Sort sort) {
        return this.taskJpaRepository.findAllByDeletedAtIsNull(sort);
    }

    @Override
    public Task getById(Long id) {
        // TODO: exception 정의
        return this.taskJpaRepository.findById(id)
                .orElseThrow(() -> new HttpStatusException(ErrorCode.TASK_NOT_FOUND));
    }

    @Override
    public Task getTaskWithCommentByTaskIdAndCommentId(Long taskId, UUID commentId) {
        return taskJpaRepository.findTaskWithCommentByTaskIdAndCommentId(taskId, commentId)
                .orElseThrow(() -> new HttpStatusException(ErrorCode.TASK_NOT_FOUND));
    }
}
