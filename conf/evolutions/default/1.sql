# Heroes schema

# --- !Ups

CREATE TABLE Hero (
    id integer(10) NOT NULL AUTO_INCREMENT,
    name varchar(255) NOT NULL,
    PRIMARY KEY(id)
);

INSERT INTO Hero values(11, 'Mr. Nice');
INSERT INTO Hero values(12, 'Narco');
INSERT INTO Hero values(13, 'Bombasto');
INSERT INTO Hero values(14, 'Celeritas');
INSERT INTO Hero values(15, 'Magenta');
INSERT INTO Hero values(16, 'RubberMan');
INSERT INTO Hero values(17, 'Dynama');
INSERT INTO Hero values(18, 'Dr IQ');
INSERT INTO Hero values(19, 'Magma');
INSERT INTO Hero values(20, 'Tornado');

# --- !Downs

DROP TABLE Hero;
