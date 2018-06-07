SHOW search_path;

CREATE SCHEMA IF NOT EXISTS game;

SET search_path TO game;

-- game
CREATE TABLE IF NOT EXISTS game.game (
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
CREATE TABLE IF NOT EXISTS game.player (
  id       SERIAL NOT NULL PRIMARY KEY,
  username VARCHAR(20) UNIQUE,
  password VARCHAR(100)
);
TRUNCATE TABLE player CASCADE;

