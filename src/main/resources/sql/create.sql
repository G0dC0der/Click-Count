DROP DATABASE clickcount_db;
CREATE DATABASE clickcount_db;
USE clickcount_db;

CREATE TABLE users
(
    Username  VARCHAR(50)  NOT NULL PRIMARY KEY,
    Password  VARCHAR(255) NOT NULL,
    Role      VARCHAR      NOT NULL,
);
INSERT INTO users (user_name, password, role) VALUES ('admin', '95f47c8ba31358731223bdf16b87eecc', 'ADMINISTRATOR');

CREATE TABLE Namespace
(
    Name     VARCHAR(50) PRIMARY KEY,
    Password VARCHAR(255)
);

CREATE TABLE URL
(
    Namespace  VARCHAR(50)  NOT NULL,
    Alias      VARCHAR(256) NOT NULL,
    Link       VARCHAR(256) NOT NULL,
    Clicks     BIGINT UNSIGNED DEFAULT 0,
    CONSTRAINT URL_PK PRIMARY KEY(Namespace, Alias),
    CONSTRAINT URL_FK FOREIGN KEY my_fk (Namespace) REFERENCES Namespace(Name)
);