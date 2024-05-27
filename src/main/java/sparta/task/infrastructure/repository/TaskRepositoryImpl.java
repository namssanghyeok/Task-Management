package sparta.task.infrastructure.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import sparta.task.domain.model.Task;
import sparta.task.domain.repository.TaskRepository;
import sparta.task.infrastructure.repository.jpa.TaskJpaRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class TaskRepositoryImpl implements TaskRepository {
    private final TaskJpaRepository taskJpaRepository;

    @Override
    public Task save(Task task) {
        return this.taskJpaRepository.save(task);
    }

    @Override
    public Optional<Task> findById(Long id) {
        return this.taskJpaRepository.findById(id);
    }

    @Override
    public List<Task> findAllByDeletedAtIsNull(Sort sort) {
        return this.taskJpaRepository.findAllByDeletedAtIsNull(sort);
    }

    @Override
    public Task getById(Long id) {
        // TODO: exception 정의
        return this.taskJpaRepository.findById(id)
                .orElseThrow();
    }
}
