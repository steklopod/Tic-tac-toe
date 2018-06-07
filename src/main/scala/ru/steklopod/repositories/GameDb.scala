package ru.steklopod.repositories

import ru.steklopod.entities.{Game, Helper, Player}
import scalikejdbc._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import ru.steklopod.repositories.ConnectionAccesNamesStore._

object GameDb  {
  val SHEMA_NAME = "game"

  def init(): Unit = {
    Class.forName(DRIVER_MARIA_DB)
    ConnectionPool.singleton(URL_MARIA, LOGIN_MARIA, PSWRD_MARIA)
  }

  def createSchema(): Boolean = {
    DB autoCommit { implicit session =>
      sql"""
           CREATE SCHEMA IF NOT EXISTS game
        """
        .execute.apply()
    }
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
            )
      """
        .execute.apply()
      truncateGame()
      Game.create(new Game(1, null, false, "1, 2", 0, Helper.ThreeByThree.toString, 3, "0, 0, 0, 0, 0, 0, 0, 0, 0"))

    }
  }

  def createPlayerTableAndTestGamer(): Future[Boolean] = {
    DB futureLocalTx { implicit session =>
      sql"""
            CREATE TABLE IF NOT EXISTS player (id SERIAL NOT NULL PRIMARY KEY, username VARCHAR(20) UNIQUE, password VARCHAR(100))
        """
        .execute.apply()
      truncatePlayer()
      Player.create(new Player("testName", "Test password"))
    }
  }

  def truncatePlayer(): Boolean = {
    DB autoCommit { implicit session =>
      sql"""
           TRUNCATE TABLE player
        """
        .execute.apply()
    }
  }

  def truncateGame(): Boolean = {
    DB autoCommit { implicit session =>
      sql"""
           TRUNCATE TABLE game
        """
        .execute.apply()
    }
  }

  def truncateAll(): Boolean = {
    truncateGame()
    truncatePlayer()
  }

}
