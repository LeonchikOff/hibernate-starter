drop table if exists users;
drop table if exists all_sequences_table;
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





