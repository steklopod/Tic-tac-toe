package ru.steklopod

import scalikejdbc.{ConnectionPool, _}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object TeapotDb {
  def init(): Unit = {
    Class.forName("org.h2.Driver")
    ConnectionPool.singleton("jdbc:h2:mem:teapot;DB_CLOSE_DELAY=-1", "", "")
  }

  def createTablesAndSomeData(): Future[Boolean] = {
    DB futureLocalTx { implicit session =>
      sql"""
      create table teapots (
        id serial not null primary key,
        volume int not null,
        max_temperature int not null,
        power float not null,
        color varchar (20) not null,
        backlight varchar (20) null)
      """.execute.apply()
      Teapot.create(Teapot(1, 1, 100, 1.5, Color.Blue, Some(Color.Green)))
    }
  }
}
