CREATE TABLE IF NOT EXISTS lab_works
(
    id             SERIAL PRIMARY KEY,
    name           VARCHAR   NOT NULL CHECK (name <> ''),
    coordinates_id BIGINT    NOT NULL,
    creation_date  TIMESTAMP NOT NULL,
    description    TEXT      NOT NULL CHECK (name <> ''),
    discipline_id  BIGINT    NOT NULL,
    difficulty     varchar(255)
        check ((difficulty)::text = ANY
               ((ARRAY ['VERY_EASY'::character varying, 'NORMAL'::character varying, 'TERRIBLE'::character varying])::text[])),
    minimal_point  INT CHECK (minimal_point > 0),
    average_point  REAL CHECK (average_point > 0),
    author_id      BIGINT    NOT NULL,
    user_id        BIGINT    NOT NULL,
    FOREIGN KEY (coordinates_id) REFERENCES coordinates (id) ON DELETE RESTRICT,
    FOREIGN KEY (discipline_id) REFERENCES disciplines (id) ON DELETE RESTRICT,
    FOREIGN KEY (author_id) REFERENCES persons (id) ON DELETE RESTRICT,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);
