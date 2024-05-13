create table task
(
    task_id   bigint primary key auto_increment,
    title     VARCHAR(200) not null,
    content   text,
    assignee  VARCHAR(30),
    password  VARCHAR(30) not null,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP // null 이 아니면 삭제됨
);