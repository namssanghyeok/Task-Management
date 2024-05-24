CREATE TABLE refresh_token
(
    refresh_token_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    token UUID NOT NULL,
    expiry_date TIMESTAMP NOT NULL,
    user_id BIGINT NOT NULL,

    CONSTRAINT fk_token_user_id FOREIGN KEY (user_id) REFERENCES member(user_id)
)