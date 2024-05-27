package sparta.task.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sparta.task.domain.model.common.TimeStamp;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "upload_file")
public class UploadFile extends TimeStamp {
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

