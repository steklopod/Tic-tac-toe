package ru.steklopod

import scalikejdbc.{ConnectionPool, _}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object TeapotDb {
  def init(): Unit = {
    Class.forName("org.postgresql.Driver")
    ConnectionPool.singleton("jdbc:postgresql://localhost:5432/home?currentSchema=test", "postgres", "postgres")
  }

  def createTablesAndSomeData(): Future[Boolean] = {
    DB futureLocalTx { implicit session =>
      sql"""
      drop table if exists game;

      create table if not exists game (
        id                    serial      not null primary key,
        next_step             int,
        won                   int,
        finished              boolean,
        players               int [2],
        steps                 int,
        size                  int [2],
        crosses_length_to_win int,
        play_field                 int [3] [3]
      );

      INSERT INTO game (next_step, won, finished, players, steps, size, crosses_length_to_win, play_field)
      VALUES (1, null, false, '{{1}, {2}}', 6, '{{3}, {3}}', 3, '{{0, 1, 2}, {0, 0, 0}, {0, 0, 0}}');
""".execute.apply()
      Game.create(Game(
        1, null, false, [1,2], 6, [3, 3], 3, [[0, 1, 2], [0, 0, 0], [0, 0, 0]]

      ))
    }
  }
}
