package ru.steklopod

import scalikejdbc.DB

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

trait TeapotRepository {
  def createTeapot(teapot: Teapot): Future[Unit]
  def getTeapot(id: Long): Future[Option[Teapot]]
}

object DBTeapotRepository extends TeapotRepository {
  TeapotDb.init()
  Await.result(TeapotDb.createTablesAndSomeData(), Duration.Inf)
  override def createTeapot(teapot: Teapot): Future[Unit] = DB.futureLocalTx(implicit session => Teapot.create(teapot).map(_ => ()))
  override def getTeapot(id: Long): Future[Option[Teapot]] = DB.futureLocalTx(implicit session => Teapot.findById(id))
}
