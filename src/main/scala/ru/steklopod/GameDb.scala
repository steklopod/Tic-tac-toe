package ru.steklopod

import scalikejdbc.{ConnectionPool, _}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object GameDb {
  def init(): Unit = {
    //    Class.forName("org.postgresql.Driver")
    //    ConnectionPool.singleton("jdbc:postgresql://localhost:5432/home?currentSchema=test", "postgres", "postgres")()
    Class.forName("org.mariadb.jdbc.Driver")
    ConnectionPool.singleton("jdbc:mariadb://127.0.0.1:3306/test", "root", "root")()
  }

  def createTablesAndSomeData(): Future[Boolean] = {
    DB futureLocalTx { implicit session =>
      sql"""
      create table if not exists game (
        id                    serial      not null primary key,
        next_step             int,
        won                   int,
        finished              boolean,
        players               text,
        steps                 int,
        size                  text,
        crosses_length_to_win    int,
        field_play             text
      );
""".execute.apply()
      Game.create(new Game
      (1,
        null,
        false,
        "1, 2",
        0,
        Helper.ThreeByThree.toString,
        3,
        "0, 0, 0, 0, 0, 0, 0, 0, 0"
      )
      )
    }
  }
}
