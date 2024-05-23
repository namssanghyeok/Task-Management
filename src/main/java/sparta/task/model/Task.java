package sparta.task.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import sparta.task.dto.request.UpdateTaskRequestDto;
import sparta.task.model.common.TimeStamp;

import java.util.List;

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
        return user.getId().equals(getAuthor().getId()) || user.getId().equals(getAssignee().getId()) || user.isAdmin();
    }

    @Transient
    public void delete() {
        super.delete();
    }


    @Transient
    public boolean isDeleted() {
        return this.getDeletedAt() != null;
    }
}
