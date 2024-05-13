package sparta.task.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sparta.task.model.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {
}
