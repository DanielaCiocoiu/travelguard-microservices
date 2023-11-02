INSERT INTO roles(name) VALUES('ROLE_USER');
INSERT INTO roles(name) VALUES('ROLE_MODERATOR');
INSERT INTO roles(name) VALUES('ROLE_ADMIN');

INSERT INTO users(username, email, password) VALUES('Vasia', 'Vasia@gmail.com', '12345678910111213141516');
INSERT INTO users(username, email, password) VALUES('Ina', 'Ina@gmail.com', '12345678910111213141516');
INSERT INTO users(username, email, password) VALUES('Maia', 'Maia@gmail.com', '12345678910111213141516');

--DROP TABLE IF EXISTS roles;
--  CREATE TABLE roles (
--    id int NOT NULL  AUTO_INCREMENT,
--    name varchar(20),
--    PRIMARY KEY (id));
--
--
--DROP TABLE IF EXISTS users;
--CREATE TABLE users (
--  id  bigint  NOT NULL  AUTO_INCREMENT,
--  username varchar(20),
--  email varchar(50),
--  password varchar(120),
--  PRIMARY KEY (id));
--
--
--DROP TABLE IF EXISTS user_roles;
--CREATE TABLE user_roles (
--user_id bigint NOT NULL ,
--role_id INT NOT NULL
-- foreign key (user_id)
--        references users);