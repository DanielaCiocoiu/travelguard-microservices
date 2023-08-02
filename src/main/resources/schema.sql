DROP TABLE IF EXISTS policies;

CREATE TABLE policies (
  id int NOT NULL  AUTO_INCREMENT,
  eligibility int NOT NULL,
  validity Boolean NOT NULL,
  type varchar(60) NOT NULL,
  benefitAndLimit varchar(60) NOT NULL,
  price double NOT NULL,
  PRIMARY KEY (id));

DROP TABLE IF EXISTS persons;

CREATE TABLE persons (
  id int NOT NULL  AUTO_INCREMENT,
  name varchar(60) NOT NULL,
  age int NOT NULL,
    nationality varchar(60) NOT NULL,
  id_policy varchar,
  PRIMARY KEY (id));

