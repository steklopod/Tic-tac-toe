
drop table game;

create table if not exists game (
  id                    serial      not null primary key,
  next_step             varchar(20) not null,
  won                   int default null,
  finished              boolean,
  players               varchar(20) [2],
  steps                 int,
  size                  int [],
  crosses_length_to_win int,
  field                 int [3] [3]
);

INSERT INTO game (next_step, won, finished, players, steps, size, crosses_length_to_win, field)
VALUES ('Test player', null, false, '{{Test player}, {Robot}}', 6, '{{3}, {3}}', 3, '{{0, 1, 2}, {0, 0, 0}, {0, 0, 0}}')

