DROP DATABASE clickcount_db;
CREATE DATABASE clickcount_db;
USE clickcount_db;

CREATE TABLE groups(
    id INT NOT NULL auto_increment,
	group_name VARCHAR(50) NOT NULL,
	password VARCHAR(50) NOT NULL,
	PRIMARY KEY (id)
);

CREATE TABLE urls(
	id INT NOT NULL auto_increment,
	url VARCHAR(80) NOT NULL,
	link VARCHAR(200) NOT NULL,
	add_date TIMESTAMP,
	clicks BIGINT UNSIGNED NOT NULL,
	group_fk INT,
	PRIMARY KEY (id),
	FOREIGN KEY (group_fk) REFERENCES groups(id)
);

CREATE TABLE users(
    user_name VARCHAR(50) NOT NULL,
    password VARCHAR(50) NOT NULL,
    role VARCHAR(50) NOT NULL,
    PRIMARY KEY (user_name)
);

CREATE UNIQUE INDEX unique_group ON groups(group_name);
CREATE UNIQUE INDEX username_index ON users(user_name);
CREATE INDEX url_index ON urls(url);
INSERT INTO groups (group_name, password) VALUES ('default', '');
INSERT INTO users (user_name, password, role) VALUES ('admin', '95f47c8ba31358731223bdf16b87eecc', 'ADMINISTRATOR');