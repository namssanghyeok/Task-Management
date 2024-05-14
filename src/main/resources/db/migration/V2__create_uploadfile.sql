create table upload_file
(
    file_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    filename VARCHAR(36) NOT NULL, // UUID
    original_file_name varchar(100) NOT NULL,
    task_id BIGINT,
    CONSTRAINT fk_file_task_id FOREIGN KEY (task_id) REFERENCES task (task_id),

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP // null 이 아니면 삭제됨
)