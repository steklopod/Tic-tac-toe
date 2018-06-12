package ru.steklopod.repositories

import ru.steklopod.entities.Player
import scalikejdbc._
import scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent.Future

object PlayerDb{

  def createPlayerTableAndTestGamer(): Future[Boolean] = {
    DB futureLocalTx { implicit session =>
      sql"""
            DROP TABLE IF EXISTS player;
        """.execute.apply()

      sql"""
            CREATE TABLE IF NOT EXISTS player (
            id       SERIAL NOT NULL PRIMARY KEY,
            username VARCHAR(20) UNIQUE,
            password VARCHAR(100),
            online BOOLEAN,
            wins INT,
            losses INT)
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

}