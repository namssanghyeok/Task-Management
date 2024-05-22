package sparta.task.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class CustomErrorResponse {
    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private String path;

    @Override
    public String toString() {
        return String.format("""
                        {
                            "timestamp": "%s",
                            "status": %d,
                            "error": "%s",
                            "message": "%s",
                            "path": "%s"
                        }
                        """,
                LocalDateTime.now(),
                this.status,
                this.error,
                this.message,
                this.path
        );
    }
}

