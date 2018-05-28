package ru.steklopod

import scalikejdbc.DB

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

trait TeapotRepository {
  def createTeapot(teapot: Game): Future[Unit]

  def getTeapot(id: Long): Future[Option[Game]]
}

object DBTeapotRepository extends TeapotRepository {
  TeapotDb.init()
  Await.result(TeapotDb.createTablesAndSomeData(), Duration.Inf)

  override def createTeapot(teapot: Game): Future[Unit] =
    DB.futureLocalTx(implicit session => Game.create(teapot).map(_ => ()))

  override def getTeapot(id: Long): Future[Option[Game]] =
    DB.futureLocalTx(implicit session => Game.findById(id))
}
