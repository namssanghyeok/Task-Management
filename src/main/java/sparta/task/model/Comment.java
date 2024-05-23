package sparta.task.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sparta.task.model.common.TimeStamp;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "comment")
public class Comment extends TimeStamp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private long id;

    private String content;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;

    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;

    @Transient
    public boolean canUpdateBy(User currentUser) {
        return author.isAdmin() || author.getId().equals(currentUser.getId());
    }

    @Transient
    public void update(Comment comment) {
        this.content = comment.getContent();
    }
}
