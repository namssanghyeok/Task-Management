package sparta.task.infrastructure.repository.jpa;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sparta.task.domain.model.Task;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskJpaRepository extends JpaRepository<Task, Long> {
    List<Task> findAllByDeletedAtIsNull(Sort sort);


//    @Modifying
//    @Transactional
//    @Query("DELETE FROM Comment c WHERE c.id = :commentId AND c.task.id = :taskId")
//    void deleteCommentByIdAndTaskId(@Param("commentId") Long commentId, @Param("taskId") Long taskId);

    @Query("SELECT t FROM Task t LEFT JOIN FETCH t.comments c WHERE t.id = :taskId AND c.id = :commentId")
    Optional<Task> findTaskWithCommentByTaskIdAndCommentId(@Param("taskId") Long taskId, @Param("commentId") UUID commentId);
}
