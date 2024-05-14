create table upload_file
(
    file_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    filename VARCHAR(36) NOT NULL, // UUID
    original_filename VARCHAR(100) NOT NULL,
    task_id BIGINT,
    size BIGINT,
    type VARCHAR(40),
    url VARCHAR(100),
    CONSTRAINT fk_file_task_id FOREIGN KEY (task_id) REFERENCES task (task_id),

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP // null 이 아니면 삭제됨
)