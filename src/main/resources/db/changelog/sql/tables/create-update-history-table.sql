CREATE TABLE IF NOT EXISTS update_history
(
    id          SERIAL PRIMARY KEY,
    lab_work_id BIGINT       NOT NULL,
    user_id     BIGINT       NOT NULL,
    action      varchar(255) not null check ((action)::text = ANY
                                             ((ARRAY ['CREATE'::character varying, 'UPDATE'::character varying, 'DELETE'::character varying])::text[])),
    action_time TIMESTAMP    NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE RESTRICT
);
