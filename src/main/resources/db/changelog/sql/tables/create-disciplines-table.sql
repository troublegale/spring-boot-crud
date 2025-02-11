CREATE TABLE IF NOT EXISTS disciplines
(
    id            SERIAL PRIMARY KEY,
    name          VARCHAR NOT NULL,
    lecture_hours INT,
    user_id       BIGINT  NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);
