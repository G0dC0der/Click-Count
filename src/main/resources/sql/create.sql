DROP DATABASE clickcount_db;
CREATE DATABASE clickcount_db;
USE clickcount_db;

CREATE TABLE Collaborator
(
    Username  VARCHAR(50)  PRIMARY KEY,
    Password  VARCHAR(255) NOT NULL,
    Role      VARCHAR(20)  NOT NULL
);

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
    Added      BIGINT UNSIGNED NOT NULL,
    PRIMARY KEY (Namespace, Alias),
    FOREIGN KEY (Namespace) REFERENCES Namespace(Name)
);

CREATE TABLE Client
(
    Identifier VARCHAR(15)     PRIMARY KEY,
    Expire     BIGINT UNSIGNED NOT NULL,
    Namespace  VARCHAR(50)     NOT NULL,
    Alias      VARCHAR(255)    NOT NULL,
    FOREIGN KEY (Namespace, Alias) REFERENCES URL(Namespace, Alias)
);

INSERT INTO Collaborator (Username, Password, Role) VALUES ('admin', '95f47c8ba31358731223bdf16b87eecc', 'ADMINISTRATOR');