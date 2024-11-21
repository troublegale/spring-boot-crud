create type difficulty as enum ('NORMAL', 'HARD', 'VERY_HARD', 'IMPOSSIBLE', 'INSANE');

create type color as enum ('GREEN', 'BLACK', 'BLUE', 'YELLOW', 'WHITE');

create table if not exists coordinates
(
    id         serial primary key,
    x          bigint not null,
    y          int,
    labwork_id int    not null references labworks (id)
);

create table if not exists disciplines
(
    id            serial primary key,
    name          text not null check (name <> ''),
    lecture_hours int,
    labwork_id    int  not null references labworks (id),
    user_id       int  not null references users (id)
);

create table if not exists locations
(
    id        serial primary key,
    x         double precision not null,
    y         double precision,
    name      text             not null,
    person_id int              not null references persons (id)
);

create table if not exists persons
(
    id          serial primary key,
    name        text   not null check (name <> ''),
    eye_color   color  not null,
    hair_color  color,
    location_id int    not null references locations (id),
    birthday    timestamp,
    height      double precision check (height > 0),
    weight      bigint not null check (weight > 0),
    user_id     int    not null references users (id)
);

create table if not exists users
(
    id              serial primary key,
    username        text not null,
    password_digest text not null,
    salt            text not null
);

create table if not exists roles
(
    id   serial primary key,
    name text not null
);

create table if not exists users_roles
(
    user_id int references users (id),
    role_id int references roles (id),
    primary key (user_id, role_id)
);

create table if not exists labworks
(
    id             serial primary key,
    name           text             not null check (name <> ''),
    coordinates_id int              not null references coordinates (id),
    creation_date  timestamp        not null default current_date,
    description    text check (description <> ''),
    difficulty     difficulty,
    discipline_id  int              not null references disciplines (id),
    minimal_point  double precision not null check (minimal_point > 0),
    average_point  double precision check (average_point > 0),
    person_id      int              not null references persons (id),
    user_id        int              not null references users (id)
);
