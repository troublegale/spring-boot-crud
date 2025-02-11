CREATE TABLE IF NOT EXISTS role_change_tickets
(
    id          SERIAL PRIMARY KEY,
    user_id     BIGINT       NOT NULL,
    role        varchar(255) not null
        check ((role)::text = ANY
               ((ARRAY ['ROLE_USER'::character varying, 'ROLE_ADMIN'::character varying])::text[])),
    status      varchar(255) not null
        check ((status)::text = ANY
               ((ARRAY ['PENDING'::character varying, 'APPROVED'::character varying, 'REJECTED'::character varying])::text[])),
    resolver_id BIGINT,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (resolver_id) REFERENCES users (id) ON DELETE SET NULL
);
