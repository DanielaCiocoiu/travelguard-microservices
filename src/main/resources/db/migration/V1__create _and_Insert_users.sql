  DROP TABLE IF EXISTS roles;
  CREATE TABLE roles (
    id int NOT NULL  AUTO_INCREMENT,
    name varchar(20),
    PRIMARY KEY (id));

INSERT INTO roles(name) VALUES('ROLE_USER');
INSERT INTO roles(name) VALUES('ROLE_MODERATOR');
INSERT INTO roles(name) VALUES('ROLE_ADMIN');

DROP TABLE IF EXISTS users;
CREATE TABLE users (
  id int NOT NULL  AUTO_INCREMENT,
  username varchar(20),
  email varchar(50),
  password varchar(120),
   PRIMARY KEY (id));








