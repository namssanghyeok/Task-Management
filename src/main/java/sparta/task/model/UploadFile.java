package sparta.task.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import sparta.task.model.common.Base;

@Getter
@Entity
@SuperBuilder
@NoArgsConstructor
@Table(name = "upload_file")
public class UploadFile extends Base {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_id")
    private Long id;

    private String filename;

    private String originalFilename;

    private String url;

    private Long taskId;

    private Long size;

    private String type;

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }
}

