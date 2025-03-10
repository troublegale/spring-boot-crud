CREATE TABLE IF NOT EXISTS import_history
(
    id            SERIAL PRIMARY KEY,
    file_name     VARCHAR     NOT NULL,
    user_id       BIGINT      NOT NULL REFERENCES users (id),
    import_status varchar(64) NOT NULL
        check ((import_status)::text = ANY
               ((ARRAY ['PROCESSING'::character varying, 'IMPORTED'::character varying, 'REJECTED'::character varying])::text[])),
    import_number INT         NOT NULL
);
