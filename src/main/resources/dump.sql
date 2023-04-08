DROP TABLE IF EXISTS users;

CREATE TABLE users
(
    userName  varchar(128) primary key,
    firstName varchar(128),
    lastName  varchar(128),
    birthDay  date,
    age       int
);