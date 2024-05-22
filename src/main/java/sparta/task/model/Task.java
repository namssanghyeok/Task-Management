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

    private String assignee;

    private String password;

    @OneToMany(mappedBy = "taskId")
    private List<UploadFile> attachments;

    // domain logic
    public void updateBy(UpdateTaskRequestDto updateTaskRequestDto) {
        if (updateTaskRequestDto.getTitle() != null && !updateTaskRequestDto.getTitle().isEmpty()) {
            this.title = updateTaskRequestDto.getTitle();
        }
        if (updateTaskRequestDto.getContent() != null && !updateTaskRequestDto.getContent().isEmpty()) {
            this.content = updateTaskRequestDto.getContent();
        }
        if (updateTaskRequestDto.getAssignee() != null && !updateTaskRequestDto.getAssignee().isEmpty()) {
            this.assignee = updateTaskRequestDto.getAssignee();
        }
    }

    public void delete() {
        super.delete();
    }

    @Transient
    public boolean checkPassword(String password) {
        return !this.password.equals(password);
    }

    @Transient
    public boolean isDeleted() {
        return this.getDeletedAt() != null;
    }
}
