drop table if exists users;
drop table if exists all_sequences_table;
drop sequence if exists users_id_seq;

create table users
(
--     id bigserial primary key,
    first_name varchar(128) not null,
    last_name  varchar(128) not null,
    birth_date date not null,
    user_name  varchar(128) unique,
    role varchar(32),
    info jsonb,
    primary key (first_name, last_name, birth_date)
);

-- create table all_sequences_table
-- (
--     table_name varchar(32) primary key,
--     pk_value   bigint not null
-- );
--
-- create sequence if not exists users_id_seq
--     owned by public.users.id;





