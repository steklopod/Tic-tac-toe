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
WHERE TIMESTAMPDIFF(MINUTE, CURRENT_TIMESTAMP(), created) < 5;


DELETE
FROM sessions
WHERE TIMESTAMPDIFF(MINUTE, CURRENT_TIMESTAMP(), created) < 5;

SELECT CURRENT_TIMESTAMP;

TRUNCATE TABLE sessions;