package ru.steklopod.repositories

import ru.steklopod.entities.Game
import scalikejdbc.DB

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

trait GameRepository {
  def createGame(game: Game): Game

  def getGame(id: Long): Future[Option[Game]]
}

object DBGameRepository extends GameRepository {
  GameDb.init()
  GameDb.createSchema()
  GameDb.createGameTablesAndEmptyGame()

//  override def createGame(game: Game): Future[Unit] =
//    DB.futureLocalTx(implicit session => Game.create(game).map(_ => ()))

  override def createGame(game: Game): Game =
    DB.autoCommit(implicit session => Game.create(game))

  override def getGame(id: Long): Future[Option[Game]] =
    DB.futureLocalTx(implicit session => Game.findById(id))
}
