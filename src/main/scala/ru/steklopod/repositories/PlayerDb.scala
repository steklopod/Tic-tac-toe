package ru.steklopod.repositories

import ru.steklopod.entities.Player
import scalikejdbc._
import scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent.Future

object PlayerDb {

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

  def createSessionTable(): Boolean = {
    DB autoCommit { implicit session =>
      sql"""
           DROP TABLE IF EXISTS sessions
         """.execute.apply()
      sql"""
           CREATE TABLE IF NOT EXISTS sessions (
             id      SERIAL NOT NULL PRIMARY KEY,
             session TEXT,
             created TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP)
        """.execute.apply()
    }
  }

  def isSessionExist(sessionValue: String): Boolean = {
    DB readOnly { implicit session =>
      val s: Option[String] = SQL("SELECT * FROM sessions WHERE created >= DATE_SUB(NOW(), INTERVAL 5 MINUTE) AND `session` = ?")
        .bind(sessionValue)
        .map(rs => rs.string("session"))
        .single.apply()
      s.isDefined
    }
  }

  def createSession(sessionValue: String) = {
    DB autoCommit { implicit session =>
      SQL(s"INSERT INTO `sessions` (session) VALUES ('${sessionValue}')")
        .update.apply()
    }
  }

  def deleteSession(sessionValue: String): Boolean = {
    DB autoCommit { implicit session =>
      SQL("DELETE FROM sessions WHERE `session` = ?")
        .bind(sessionValue)
        .execute.apply()
    }
  }

  val deleteOldSessions = new Runnable {
    def run() {
        DB autoCommit { implicit session =>
          sql"""
      DELETE FROM sessions WHERE created <= DATE_SUB(NOW(), INTERVAL 5 MINUTE)
     """.execute.apply()
        }
      }
    }
}