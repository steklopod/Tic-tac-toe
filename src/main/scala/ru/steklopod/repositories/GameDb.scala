package ru.steklopod.repositories

import ru.steklopod.entities.{Game, Player}
import ru.steklopod.repositories.ConnectionAccesNamesStore._
import ru.steklopod.util.GameFieldConverter
import scalikejdbc._
import ru.steklopod.repositories.PlayerDb._
import scala.concurrent.ExecutionContext.Implicits.global

object GameDb {
  val SHEMA_NAME = "game"

  def init(): Unit = {
    ConnectionPool.singleton(new DataSourceConnectionPool(DataSource()))
  }

  def createSchema(): Boolean = {
    DB autoCommit { implicit session =>
      sql"""
           CREATE SCHEMA IF NOT EXISTS game
        """
        .execute.apply()
    }
  }

  def createGameTablesAndEmptyGame(): Game = {
    DB autoCommit  { implicit session =>
      sql"""
            DROP TABLE IF EXISTS game
        """.execute.apply()
      sql"""
            CREATE TABLE IF NOT EXISTS game (
              id                    SERIAL NOT NULL PRIMARY KEY,
              next_step             TEXT,
              won                   TEXT,
              finished              BOOLEAN,
              players               TEXT,
              steps                 INT,
              size                  TEXT,
              crosses_length_to_win INT,
              field_play            TEXT
            )
      """
        .execute.apply()

      Game.create(new Game("Vasya", null, false, "Vasya, Nagibator",
        0, Vector(3,3), 3, GameFieldConverter.makeFieldFromSize(Vector(3,3))))
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
