package ru.steklopod.repositories

import ru.steklopod.entities.Player
import scalikejdbc.DB

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

trait PlayerRepository {
  def createPlayer(player: Player): Future[String]

  def getPlayer(id: Long): Future[Option[Player]]

  def findByName(username: String): Future[Option[Player]]

}

object DBPlayerRepository extends PlayerRepository {
  GameDb.init()
  Await.result(GameDb.createPlayerTableAndTestGamer(), Duration.Inf)

  override def createPlayer(player: Player): Future[String] =
    DB.futureLocalTx(implicit session => Player
      .create(player)
      .map(_ => player.username)
    )
      .recover { case _ => s"Player with such name is exist" }

  override def getPlayer(id: Long): Future[Option[Player]] =
    DB.futureLocalTx(implicit session => Player.findById(id))

  override def findByName(username: String): Future[Option[Player]] =
    DB.futureLocalTx(implicit session => Player.findByName(username))

}
