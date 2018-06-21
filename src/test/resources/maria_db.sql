CREATE SCHEMA IF NOT EXISTS game;
USE game;

#######
-- game
DROP TABLE IF EXISTS game;

CREATE TABLE IF NOT EXISTS game (
  id                    SERIAL NOT NULL PRIMARY KEY,
  next_step             TEXT,
  won                   INT,
  finished              BOOLEAN,
  players               TEXT,
  steps                 INT,
  size                  TEXT,
  crosses_length_to_win INT,
  field_play            TEXT
);
TRUNCATE TABLE game;

SELECT MAX(id)
FROM GAME;

SELECT *
FROM game
LIMIT 2;

#########
-- player
DROP TABLE IF EXISTS player;

CREATE TABLE IF NOT EXISTS player (
  id       SERIAL NOT NULL PRIMARY KEY,
  username VARCHAR(20) UNIQUE,
  password VARCHAR(100),
  online   BOOLEAN,
  wins     INT,
  losses   INT
);
TRUNCATE TABLE player;

###########
-- sessions
CREATE TABLE IF NOT EXISTS sessions (
  id      SERIAL NOT NULL PRIMARY KEY,
  session TEXT,
  created TIMESTAMP DEFAULT CURRENT_TIMESTAMP
  ON UPDATE CURRENT_TIMESTAMP
);

SELECT EXISTS(SELECT session FROM sessions where session = 'abc');

SELECT *
FROM sessions
WHERE (CURRENT_TIMESTAMP() - created) > 1 * 5;


SELECT * FROM sessions WHERE TIMESTAMPDIFF(MINUTE, CURRENT_TIMESTAMP(), created) < 5;


SELECT *
FROM sessions
WHERE created >= (TIMESTAMPDIFF(MINUTE, 5, CURRENT_DATE));


INSERT INTO `sessions` (session)
VALUES ($2a$10$ilYozpv4TYAiL9Jn6XedjeJ5QGZwPlgLyM.Ctjg5foBVDyxJYB8 / C);


SELECT session
FROM sessions
WHERE created < DATE_SUB(NOW(), INTERVAL 5 MINUTE);

DELETE
FROM sessions
WHERE created <= DATE_SUB(NOW(), INTERVAL 5 MINUTE);


SELECT CURRENT_TIMESTAMP;