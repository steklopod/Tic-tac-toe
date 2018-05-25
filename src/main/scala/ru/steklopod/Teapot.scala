package ru.steklopod

import scalikejdbc._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

final case class Teapot(id: Long, volume: Int, maxTemperature: Int, power: Double, color: Color, backlight: Option[Color])

sealed trait Color

object Color {

  final case object Red extends Color {
    override def toString: String = "red"
  }

  final case object Blue extends Color {
    override def toString: String = "blue"
  }

  final case object Green extends Color {
    override def toString: String = "green"
  }

  def fromString(s: String): Color = s match {
    case "red" => Color.Red
    case "blue" => Color.Blue
    case "green" => Color.Green
    case c => throw new IllegalArgumentException(s"There're no color $c")
  }
}

object Teapot extends SQLSyntaxSupport[Teapot] {

  override val tableName = "teapots"

  def apply(r: ResultName[Teapot])(rs: WrappedResultSet) =
    new Teapot(
      rs.long(r.id),
      rs.int(r.volume),
      rs.int(r.maxTemperature),
      rs.double(r.power),
      Color.fromString(rs.string(r.color)),
      rs.stringOpt(r.backlight).map(Color.fromString)
    )

  private val t = syntax

  def create(teapot: Teapot)(implicit session: DBSession = AutoSession): Future[Boolean] = {
    val sql = withSQL(insert.into(Teapot).namedValues(
      column.id -> teapot.id,
      column.volume -> teapot.volume,
      column.maxTemperature -> teapot.maxTemperature,
      column.power -> teapot.power,
      column.color -> teapot.color.toString,
      column.backlight -> teapot.backlight.map(_.toString)
    ))
    Future {
      sql.update().apply() == 1
    }
  }

  def findById(id: Long)(implicit s: DBSession = AutoSession): Future[Option[Teapot]] = Future {
    val sql = withSQL(
      select.from[Teapot](Teapot as t).where.eq(t.id, id))
    sql.map(Teapot(t.resultName)).headOption().apply()
  }
}