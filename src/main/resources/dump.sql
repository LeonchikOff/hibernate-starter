DROP TABLE IF EXISTS users;

CREATE TABLE users
(
    user_name  varchar(128) primary key,
    first_name varchar(128),
    last_name  varchar(128),
    birth_date  date,
    age       int
);

alter table users add role varchar(32);