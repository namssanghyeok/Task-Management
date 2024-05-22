ALTER TABLE task
ADD COLUMN user_id BIGINT;

ALTER TABLE task
    ADD CONSTRAINT fk_task_user_id
        FOREIGN KEY (user_id)
            REFERENCES member (user_id);
