CREATE TABLE IF NOT EXISTS persons
(
    id          SERIAL PRIMARY KEY,
    name        VARCHAR            NOT NULL CHECK (name <> ''),
    eye_color   varchar(255)       not null check ((eye_color)::text = ANY
                                                   ((ARRAY ['RED'::character varying, 'WHITE'::character varying, 'BROWN'::character varying])::text[])),
    hair_color  varchar(255)       not null check ((eye_color)::text = ANY
                                                   ((ARRAY ['RED'::character varying, 'WHITE'::character varying, 'BROWN'::character varying])::text[])),
    location_id BIGINT,
    passport_id VARCHAR(40) UNIQUE NOT NULL,
    nationality varchar(255)       not null
        check ((nationality)::text = ANY
               ((ARRAY ['UNITED_KINGDOM'::character varying, 'USA'::character varying, 'CHINA'::character varying, 'SOUTH_KOREA'::character varying])::text[])),
    user_id     BIGINT             NOT NULL,
    FOREIGN KEY (location_id) REFERENCES locations (id) ON DELETE RESTRICT,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);
