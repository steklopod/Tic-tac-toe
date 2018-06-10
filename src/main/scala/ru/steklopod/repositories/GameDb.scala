package ru.steklopod.repositories

import javax.sql.DataSource
import ru.steklopod.entities.{Game, Player}
import ru.steklopod.repositories.ConnectionAccesNamesStore._
import ru.steklopod.util.GameFieldConverter
import scalikejdbc._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


object GameDb {
  val SHEMA_NAME = "game"

  object DataSource {
    import com.zaxxer.hikari._
    private[this] lazy val dataSource: DataSource = {
      val ds = new HikariDataSource()
      ds.setDriverClassName(DRIVER_MARIA_DB)
      ds.setJdbcUrl(URL_MARIA)
      ds.setPassword(PSWRD_MARIA)
      ds.setUsername(LOGIN_MARIA)
      ds
    }
    def apply(): DataSource = dataSource
  }

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

  def createPlayerTableAndTestGamer(): Future[Boolean] = {
    DB futureLocalTx { implicit session =>
      sql"""
            DROP TABLE IF EXISTS player;
        """.execute.apply()

      sql"""
            CREATE TABLE IF NOT EXISTS player (id SERIAL NOT NULL PRIMARY KEY, username VARCHAR(20) UNIQUE, password VARCHAR(100))
        """
        .execute.apply()
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
