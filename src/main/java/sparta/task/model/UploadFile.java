package sparta.task.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.context.annotation.Lazy;
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

    private Long taskId;

    private Long size;

    private String type;
}
