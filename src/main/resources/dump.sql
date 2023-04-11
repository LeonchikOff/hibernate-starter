drop table if exists users_chat;
drop table if exists chat;
drop table if exists profile;
drop table if exists users;
drop table if exists company;
drop sequence if exists profile_id_seq;
drop sequence if exists users_id_seq;
drop sequence if exists company_id_seq;

create table company
(
    id   serial primary key,
    name varchar(64) unique
);

create table users
(
    id         bigserial primary key,
    user_name  varchar(128) unique,
    first_name varchar(128),
    last_name  varchar(128),
    birth_date date not null,
    role       varchar(32),
    info       jsonb,
    company_id int references company (id)
);

create table profile
(
    id bigserial primary key,
    user_id bigint not null unique references users(id),
    street varchar(128),
    language char(2)
);

create table chat
(
  id bigserial primary key,
  name varchar(64) not null unique
);

create table users_chat
(
    user_id bigint references users(id),
    chat_id bigint references chat(id),
    primary key (user_id, chat_id)
);





