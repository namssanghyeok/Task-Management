create table member
(
    user_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nickname VARCHAR(30) NOT NULL,
    username VARCHAR(10) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    role TINYINT DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP
);

create table task
(
    task_id   BIGINT PRIMARY KEY AUTO_INCREMENT,
    title     VARCHAR(200) not null,
    content   TEXT,
    assignee  VARCHAR(30),
    password  VARCHAR(30) not null,
    assignee_id BIGINT not null,
    author_id BIGINT,
    CONSTRAINT fk_task_author_id FOREIGN KEY (author_id) REFERENCES member (user_id),
    CONSTRAINT fk_task_assignee_id FOREIGN KEY (assignee_id) REFERENCES member (user_id),

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP // null 이 아니면 삭제됨
);