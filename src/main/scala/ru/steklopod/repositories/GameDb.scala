package ru.steklopod.repositories

import ru.steklopod.entities.{Game, Helper, Player}
import scalikejdbc._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object GameDb {
  def init(): Unit = {
    //    Class.forName("org.postgresql.Driver")
    //    ConnectionPool.singleton("jdbc:postgresql://localhost:5432/home?currentSchema=test", "postgres", "postgres")()
    Class.forName("org.mariadb.jdbc.Driver")
    ConnectionPool.singleton("jdbc:mariadb://127.0.0.1:3306/test", "root", "root")()

    //    Class.forName("org.h2.Driver")
    //    ConnectionPool.singleton("jdbc:h2:mem:teapot;DB_CLOSE_DELAY=-1", "", "")
  }

  def createGameTablesAndEmptyGame(): Future[Boolean] = {
    DB futureLocalTx { implicit session =>
      sql"""
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
      """
        .execute.apply()
      Game.create(new Game(1, null, false, "1, 2", 0, Helper.ThreeByThree.toString, 3, "0, 0, 0, 0, 0, 0, 0, 0, 0"))

    }
  }

  def createPlayerTableAndTestGamer(): Future[Boolean] = {
    DB futureLocalTx { implicit session =>
      sql"""
            CREATE TABLE IF NOT EXISTS player (id SERIAL NOT NULL PRIMARY KEY, username VARCHAR(20) /*UNIQUE */, password VARCHAR(100));
        """
        .execute.apply()
      Player.create(new Player("testName", "Test password"))
    }
  }

  def truncateAll(): Boolean = {
    DB autoCommit { implicit session =>
      sql"""
           TRUNCATE TABLE player
        """
        .execute.apply()
    }
  }

}
