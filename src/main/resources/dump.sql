drop table if exists users;
create table users
(
    user_name  varchar(128) primary key,
    first_name varchar(128),
    last_name  varchar(128),
    birth_date date,
    age        int
);

alter table users
    drop column age;
alter table users
    drop constraint users_pkey;
alter table users
    add column id bigserial primary key;
alter table users
    add unique (user_name);
alter table users
    add column role varchar(32);
alter table users
    add column info jsonb;

create sequence if not exists users_id_seq
    owned by public.users.id;

drop sequence if exists users_id_seq;

create table all_sequences_table(
    table_name varchar(32) primary key,
    pk_value bigint not null
);
