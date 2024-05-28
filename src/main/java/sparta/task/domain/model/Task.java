package sparta.task.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sparta.task.domain.model.common.TimeStamp;
import sparta.task.infrastructure.exception.HttpStatusException;
import sparta.task.infrastructure.exception.constants.ErrorCode;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "task")
@Entity
public class Task extends TimeStamp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id")
    private Long id;

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
    @Transient
    public void update(Task task) {
        if (task.title != null && !task.title.isEmpty()) {
            this.title = task.title;
        }
        if (task.content != null && !task.content.isEmpty()) {
            this.content = task.content;
        }
        if (task.assignee != null) {
            this.assignee = task.assignee;
        }
    }

    @Transient
    public boolean canUpdateBy(User user) {
        return user.getId().equals(getAuthor().getId())
                || user.getId().equals(getAssignee().getId())
                || user.isAdmin();
    }

    @Transient
    public boolean isDeleted() {
        return deletedAt != null;
    }

    @Transient
    public void addComment(Comment comment) {
        comments.add(comment);
        comment.setTask(this);
    }

    @Transient
    public void deleteComment(UUID commentId, User currentUser) {
        Comment comment = comments.stream()
                .filter(c -> c.getId().equals(commentId))
                .findFirst()
                .orElseThrow(() -> new HttpStatusException(ErrorCode.NOT_FOUND));

        if (!comment.canUpdateBy(currentUser)) {
            throw new HttpStatusException(ErrorCode.FORBIDDEN);
        }
        comment.delete();

        comments.removeIf(c -> c.getId().equals(commentId));
    }

    @Transient
    public Comment updateComment(UUID commentId, Comment updateComment, User currentUser) {
        Comment comment = comments.stream()
                .filter(c -> c.getId().equals(commentId))
                .findFirst()
                .orElseThrow(() -> new HttpStatusException(ErrorCode.NOT_FOUND));

        if (!comment.canUpdateBy(currentUser)) {
            throw new HttpStatusException(ErrorCode.FORBIDDEN);
        }
        comment.update(updateComment);
        return comment;
    }
}
