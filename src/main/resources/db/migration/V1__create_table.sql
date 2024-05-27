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
    assignee_id BIGINT not null,
    author_id BIGINT,
    CONSTRAINT fk_task_author_id FOREIGN KEY (author_id) REFERENCES member (user_id),
    CONSTRAINT fk_task_assignee_id FOREIGN KEY (assignee_id) REFERENCES member (user_id),

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP // null 이 아니면 삭제됨
);

create table comment
(
    comment_id uuid primary key,
    content varchar,

    task_id bigint,
    author_id bigint,

    constraint fk_comment_author_id foreign key (author_id) references member(user_id),
    constraint fk_comment_task_id foreign key(task_id) references task(task_id),

    created_at timestamp default current_timestamp,
    updated_at timestamp on update current_timestamp,
    deleted_at timestamp
)