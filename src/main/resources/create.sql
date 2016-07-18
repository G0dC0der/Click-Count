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
    Alias      VARCHAR(50)  NOT NULL,
    Source     VARCHAR(256) NOT NULL,
    Clicks     BIGINT UNSIGNED DEFAULT 0,
    Added      BIGINT UNSIGNED NOT NULL,
    PRIMARY KEY (Namespace, Alias),
    FOREIGN KEY (Namespace) REFERENCES Namespace(Name)
);

CREATE TABLE Client
(
    Identifier VARCHAR(15)     NOT NULL,
    Expire     BIGINT UNSIGNED NOT NULL,
    Namespace  VARCHAR(50)     NOT NULL,
    Alias      VARCHAR(50)     NOT NULL,
    PRIMARY KEY (Identifier, Namespace, Alias)
);

INSERT INTO Collaborator (Username, Password, Role) VALUES ('admin', '5b019eb34c608900c7ff356be3e4fba2', 'ADMINISTRATOR');
INSERT INTO Collaborator (Username, Password, Role) VALUES ('maintainer', '5b019eb34c608900c7ff356be3e4fba2', 'MAINTAINER');
INSERT INTO Collaborator (Username, Password, Role) VALUES ('trusted', '5b019eb34c608900c7ff356be3e4fba2', 'TRUSTED');
INSERT INTO Namespace (Name, Password) VALUES ('default', '');