CREATE TABLE IF NOT EXISTS users
(
    id       SERIAL PRIMARY KEY,
    username VARCHAR(64)  NOT NULL UNIQUE CHECK (LENGTH(username) >= 3),
    password VARCHAR      NOT NULL CHECK (LENGTH(password) >= 8),
    role     varchar(255) not null check ((role)::text = ANY
                                          ((ARRAY ['ROLE_USER'::character varying, 'ROLE_ADMIN'::character varying])::text[]))
);
