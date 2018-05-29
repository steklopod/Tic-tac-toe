package ru.steklopod.repositories

import ru.steklopod.entities.Player
import scalikejdbc.DB

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

trait PlayerRepository {
  def createPlayer(player: Player): Future[Unit]

  def getPlayer(id: Long): Future[Option[Player]]
}

object DBPlayerRepository extends PlayerRepository {
//  GameDb.init()
  Await.result(GameDb.createPlayerTableAndTestGamer(), Duration.Inf)

  override def createPlayer(player: Player): Future[Unit] =
    DB.futureLocalTx(implicit session => Player.create(player).map(_ => ()))


  override def getPlayer(id: Long): Future[Option[Player]] =
    DB.futureLocalTx(implicit session => Player.findById(id))

}
