DROP TABLE IF EXISTS persons;
CREATE TABLE persons (
  id int NOT NULL  AUTO_INCREMENT,
  name varchar(60) NOT NULL,
  age int NOT NULL,
  nationality varchar(60) NOT NULL,
   PRIMARY KEY (id));

DROP TABLE IF EXISTS policies;
CREATE TABLE policies (
  id int NOT NULL  AUTO_INCREMENT,
  eligibility int NOT NULL,
  validity Boolean NOT NULL,
  type varchar(60) NOT NULL,
  benefit_and_limit varchar(60) NOT NULL,
  price double NOT NULL,
  issue_date Date NOT NULL,
  last_day_coverage_period Date NOT NULL,
  id_person int,
  person_id int,
  PRIMARY KEY (id));

--ALTER TABLE persons ADD CONSTRAINT FK_PERSONS_ON_ID FOREIGN KEY (id) REFERENCES persons (id);
--ALTER TABLE policies ADD CONSTRAINT FK_POLICIES_ON_ID FOREIGN KEY (id) REFERENCES persons (id);

