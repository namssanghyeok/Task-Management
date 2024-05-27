package sparta.task.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import sparta.task.infrastructure.exception.constants.ErrorCode;
import sparta.task.domain.model.common.TimeStamp;
import sparta.task.infrastructure.exception.HttpStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@DynamicInsert
@Table(name = "task")
@Entity
public class Task extends TimeStamp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id") private Long id;

    private String title;

    private String content;

    @OneToMany(mappedBy = "taskId")
    private List<UploadFile> attachments;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;

    @ManyToOne
    @JoinColumn(name = "assignee_id")
    private User assignee;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    // domain logic
    public void update(Task task) {
        if (task.getTitle() != null && !task.getTitle().isEmpty()) {
            this.title = task.getTitle();
        }
        if (task.getContent() != null && !task.getContent().isEmpty()) {
            this.content = task.getContent();
        }
        if (task.getAssignee() != null) {
            this.assignee = task.getAssignee();
        }
    }

    @Transient
    public boolean canUpdateBy(User user) {
        return user.getId().equals(getAuthor().getId())
                || user.getId().equals(getAssignee().getId())
                || user.isAdmin();
    }

    @Transient
    public void delete(User currentUser) {
        if (this.isDeleted()) {
            throw new HttpStatusException(ErrorCode.ALREADY_DELETED);
        }
        if (!this.canUpdateBy(currentUser)) {
            throw new RuntimeException("권한없음");
        }
        super.delete();
    }


    @Transient
    public boolean isDeleted() {
        return this.deletedAt != null;
    }

    public void addComment(Comment comment) {
        comments.add(comment);
        comment.setTask(this);
    }

    public void deleteComment(UUID commentId, User currentUser) {
        Comment comment = comments.stream()
                .filter(c -> c.getId().equals(commentId))
                .findFirst().orElse(null);

        if (comment == null) {
            return;
        }

        if (!currentUser.isAdmin() && !currentUser.getId().equals(comment.getAuthor().getId())) {
            // throw exception - 본인 또는 어드민만 삭제할 수 있음
            return;
        }

        comments.removeIf(c -> c.getId().equals(commentId));
    }

    public Comment updateComment(UUID commentId, Comment updateComment, User currentUser) {
        Comment comment = comments.stream()
                .filter(c -> c.getId().equals(commentId)).findFirst().orElse(null);
        if (comment == null || !canUpdateComment(comment, currentUser)) {
            // TODO: throw exception
            return null;
        }
        comment.update(updateComment);
        return comment;
    }

    private boolean canUpdateComment(Comment comment, User currentUser) {
        return currentUser.isAdmin() || currentUser.getId().equals(comment.getAuthor().getId());
    }
}
