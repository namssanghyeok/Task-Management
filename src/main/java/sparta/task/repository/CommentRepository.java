package sparta.task.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sparta.task.model.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
