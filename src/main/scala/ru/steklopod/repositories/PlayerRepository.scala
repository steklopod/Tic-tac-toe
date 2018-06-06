package ru.steklopod.repositories

import java.sql.SQLException

import ru.steklopod.entities.Player
import scalikejdbc.DB

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

trait PlayerRepository {
  def createPlayer(player: Player): Future[Boolean]

  def getPlayer(id: Long): Future[Option[Player]]

  def findByName(username: String): Future[Option[Player]]

}

object DBPlayerRepository extends PlayerRepository {
  GameDb.init()
  Await.result(GameDb.createPlayerTableAndTestGamer(), Duration.Inf)

  override def createPlayer(player: Player): Future[Boolean] =
    DB.futureLocalTx(implicit session => Player
      .create(player)
      .map(_ => true)
    )
      .recover { case e: SQLException  => false }

  override def getPlayer(id: Long): Future[Option[Player]] =
    DB.futureLocalTx(implicit session => Player.findById(id))

  override def findByName(username: String): Future[Option[Player]] =
    DB.futureLocalTx(implicit session => Player.findByName(username))

}
