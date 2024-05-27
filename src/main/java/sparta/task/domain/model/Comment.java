package sparta.task.domain.model;

import jakarta.persistence.*;
import lombok.*;
import sparta.task.domain.model.common.TimeStamp;
import sparta.task.infrastructure.exception.HttpStatusException;
import sparta.task.infrastructure.exception.constants.ErrorCode;

import java.util.UUID;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "comment")
public class Comment extends TimeStamp {
    @Id
    @Column(name = "comment_id")
    private UUID id;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private User author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id")
    private Task task;


    public void setTask(Task task) {
        this.task = task;
    }

    @Transient
    public boolean canUpdateBy(User currentUser) {
        return author.isAdmin() || author.getId().equals(currentUser.getId());
    }

    @Transient
    public void delete(User currentUser) {
        if (this.isDeleted()) {
            throw new HttpStatusException(ErrorCode.ALREADY_DELETED);
        }
        if (!canUpdateBy(currentUser)) {
            throw new HttpStatusException(ErrorCode.FORBIDDEN);
        }
        super.delete();
    }

    @Transient
    public void update(Comment comment, User currentUser) {
        if (!this.canUpdateBy(currentUser)) {
            throw new HttpStatusException(ErrorCode.FORBIDDEN);
        }
        this.content = comment.content;
    }
}
