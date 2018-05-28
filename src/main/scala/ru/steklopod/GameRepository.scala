package ru.steklopod

import scalikejdbc.DB

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

trait GameRepository {
  def createGame(game: Game): Future[Unit]

  def getGame(id: Long): Future[Option[Game]]
}

object DBGameRepository$ extends GameRepository {
  GameDb.init()
  Await.result(GameDb.createTablesAndSomeData(), Duration.Inf)

  override def createGame(game: Game): Future[Unit] =
    DB.futureLocalTx(implicit session => Game.create(game).map(_ => ()))

  override def getGame(id: Long): Future[Option[Game]] =
    DB.futureLocalTx(implicit session => Game.findById(id))
}
