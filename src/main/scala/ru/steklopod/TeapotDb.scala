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
""".execute.apply()
      Teapot.create(Teapot(1, 1, 100, 1.5, Color.Blue, Some(Color.Green)))
    }
  }
}
