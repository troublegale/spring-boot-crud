CREATE TABLE coordinates
(
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    x  DOUBLE PRECISION,
    y  BIGINT                                  NOT NULL,
    CONSTRAINT pk_coordinates PRIMARY KEY (id)
);

CREATE TABLE disciplines
(
    id            BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name          VARCHAR(255)                            NOT NULL,
    lecture_hours INTEGER,
    CONSTRAINT pk_disciplines PRIMARY KEY (id)
);

CREATE TABLE lab_works
(
    id             BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name           VARCHAR(255)                            NOT NULL,
    coordinates_id BIGINT                                  NOT NULL,
    creation_date  TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
    description    VARCHAR(255),
    difficulty     VARCHAR(255),
    discipline_id  BIGINT                                  NOT NULL,
    minimal_point  INTEGER,
    average_point  FLOAT,
    author_id      BIGINT                                  NOT NULL,
    user_id        BIGINT                                  NOT NULL,
    CONSTRAINT pk_lab_works PRIMARY KEY (id)
);

CREATE TABLE locations
(
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    x  BIGINT,
    y  DOUBLE PRECISION                        NOT NULL,
    z  FLOAT,
    CONSTRAINT pk_locations PRIMARY KEY (id)
);

CREATE TABLE persons
(
    id          BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    eye_color   VARCHAR(255)                            NOT NULL,
    hair_color  VARCHAR(255),
    location_id BIGINT,
    passport_id VARCHAR(40),
    nationality VARCHAR(255)                            NOT NULL,
    user_id     BIGINT                                  NOT NULL,
    CONSTRAINT pk_persons PRIMARY KEY (id)
);

CREATE TABLE update_history
(
    id          BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    lab_work_id BIGINT                                  NOT NULL,
    user_id     BIGINT                                  NOT NULL,
    action      VARCHAR(255)                            NOT NULL,
    action_time TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
    CONSTRAINT pk_update_history PRIMARY KEY (id)
);

CREATE TABLE users
(
    id       BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    username VARCHAR(64)                             NOT NULL,
    password VARCHAR(255)                            NOT NULL,
    role     VARCHAR(255)                            NOT NULL,
    CONSTRAINT pk_users PRIMARY KEY (id)
);

ALTER TABLE lab_works
    ADD CONSTRAINT uc_lab_works_coordinates UNIQUE (coordinates_id);

ALTER TABLE lab_works
    ADD CONSTRAINT uc_lab_works_discipline UNIQUE (discipline_id);

ALTER TABLE persons
    ADD CONSTRAINT uc_persons_location UNIQUE (location_id);

ALTER TABLE persons
    ADD CONSTRAINT uc_persons_passport UNIQUE (passport_id);

ALTER TABLE users
    ADD CONSTRAINT uc_users_username UNIQUE (username);

ALTER TABLE lab_works
    ADD CONSTRAINT FK_LAB_WORKS_ON_AUTHOR FOREIGN KEY (author_id) REFERENCES persons (id);

ALTER TABLE lab_works
    ADD CONSTRAINT FK_LAB_WORKS_ON_COORDINATES FOREIGN KEY (coordinates_id) REFERENCES coordinates (id);

ALTER TABLE lab_works
    ADD CONSTRAINT FK_LAB_WORKS_ON_DISCIPLINE FOREIGN KEY (discipline_id) REFERENCES disciplines (id);

ALTER TABLE lab_works
    ADD CONSTRAINT FK_LAB_WORKS_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);

ALTER TABLE persons
    ADD CONSTRAINT FK_PERSONS_ON_LOCATION FOREIGN KEY (location_id) REFERENCES locations (id);

ALTER TABLE persons
    ADD CONSTRAINT FK_PERSONS_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);

ALTER TABLE update_history
    ADD CONSTRAINT FK_UPDATE_HISTORY_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);