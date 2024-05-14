package sparta.task.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import sparta.task.dto.UpdateTaskDto;
import sparta.task.model.common.Base;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SuperBuilder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "task")
public class Task extends Base {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id")
    private Long id;

    private String title;

    private String content;

    private String assignee;

    private String password;

    @OneToMany(mappedBy = "taskId")
    private List<UploadFile> attachments = new ArrayList<>();

    // domain logic
    public void updateBy(UpdateTaskDto updateTaskDto) {
        if (updateTaskDto.getTitle() != null && !updateTaskDto.getTitle().isEmpty()) {
            this.title = updateTaskDto.getTitle();
        }
        if (updateTaskDto.getContent() != null && !updateTaskDto.getContent().isEmpty()) {
            this.content = updateTaskDto.getContent();
        }
        if (updateTaskDto.getAssignee() != null && !updateTaskDto.getAssignee().isEmpty()) {
            this.assignee = updateTaskDto.getAssignee();
        }
        this.changeUpdatedAt(LocalDateTime.now());
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
