create table task
(
    task_id   BIGINT PRIMARY KEY AUTO_INCREMENT,
    title     VARCHAR(200) not null,
    content   TEXT,
    assignee  VARCHAR(30),
    password  VARCHAR(30) not null,

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP // null 이 아니면 삭제됨
);