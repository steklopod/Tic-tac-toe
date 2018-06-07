
CREATE SCHEMA IF NOT EXISTS game;

SHOW search_path;
SET search_path TO game;

-- game
CREATE TABLE IF NOT EXISTS game (
  id                    SERIAL NOT NULL PRIMARY KEY,
  next_step             INT,
  won                   INT,
  finished              BOOLEAN,
  players               TEXT,
  steps                 INT,
  size                  TEXT,
  crosses_length_to_win INT,
  field_play            TEXT
);
TRUNCATE TABLE game CASCADE;

-- player
CREATE TABLE IF NOT EXISTS player (
  id       SERIAL NOT NULL PRIMARY KEY,
  username VARCHAR(20) UNIQUE,
  password VARCHAR(100)
);
TRUNCATE TABLE player CASCADE;

