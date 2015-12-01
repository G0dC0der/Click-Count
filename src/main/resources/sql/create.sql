DROP DATABASE clickcount;
CREATE DATABASE clickcount;
USE clickcount;

CREATE TABLE groups(
	id INT NOT NULL auto_increment,
	groupname VARCHAR(50) NOT NULL,
	password VARCHAR(50) NOT NULL,
	PRIMARY KEY (id)
);

CREATE TABLE urls(
	id INT NOT NULL auto_increment,
	url VARCHAR(80) NOT NULL,
	link VARCHAR(200) NOT NULL,
	group_fk INT,
	PRIMARY KEY (id),
	FOREIGN KEY (group_fk) REFERENCES groups(id)
);

CREATE TABLE clicks(
	id INT NOT NULL auto_increment,
	click_date TIMESTAMP,
	url_fk INT,
	PRIMARY KEY (id),
	FOREIGN KEY (url_fk) REFERENCES urls(id)
);

CREATE UNIQUE INDEX unique_group ON groups(groupname);
INSERT INTO groups (groupname, password) VALUES ('default', '');