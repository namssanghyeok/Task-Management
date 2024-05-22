create table member
(
    user_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nickname VARCHAR(30) NOT NULL,
    username VARCHAR(10) NOT NULL,
    password VARCHAR(15) NOT NULL,
    role TINYINT DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP
)