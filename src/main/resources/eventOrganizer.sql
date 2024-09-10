CREATE DATABASE eventOrganizer

CREATE TABLE IF NOT EXISTS events (
    id SERIAL PRIMARY KEY,
    title varchar(250) NOT NULL,
    start_on TIMESTAMP NOT NULL,
    complete_on TIMESTAMP NOT NULL,
    participant INT NOT NULL,
    location varchar(10) NOT NULL,
    version INT
);

CREATE TABLE participants (
	id SERIAL PRIMARY KEY,
	event_id INT,
	participant_name VARCHAR(255) NOT NULL,
	email VARCHAR(255) not NULL,
	FOREIGN KEY (event_id) REFERENCES events(id) ON DELETE CASCADE
)

INSERT INTO events (title, start_on, complete_on, participant, location, VERSION)
VALUES ('Party', '2024-02-25T06:05:00.000000', '2024-02-27T10:27:00.000000', 300, 'BEKASI', 0);

INSERT INTO participants (event_id, participant_name, email)
VALUES (1, 'Danu', 'danu@gmail.com'), (2, 'Dani', 'dani@gmail.com'), (2, 'Deni', 'deni@gmail.com'), (1, 'Doni', 'doni@gmail.com');