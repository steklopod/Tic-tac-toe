CREATE SCHEMA IF NOT EXISTS game;
USE game;

-- game
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

-- player
CREATE TABLE IF NOT EXISTS player (
  id       SERIAL NOT NULL PRIMARY KEY,
  username VARCHAR(20) UNIQUE,
  password VARCHAR(100)
);
TRUNCATE TABLE player;

