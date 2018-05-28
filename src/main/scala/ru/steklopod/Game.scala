package ru.steklopod

import scalikejdbc._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

final case class Game(
//                     id: Long,
                       nextStep: Int,
                       won: Int,
                       finished: Boolean,
                       players: java.sql.Array,
                       steps: Int,
                       size: java.sql.Array,
                       crossesLengthToWin: Int,
                       field: java.sql.Array
                     )

object Game extends SQLSyntaxSupport[Game] {

  override val tableName = "game"

  def apply(r: ResultName[Game])(rs: WrappedResultSet) =
    new Game(
      rs.int(r.next_step),
      rs.int(r.won),
      rs.boolean(r.finished),
      rs.array(r.players),
      rs.int(r.steps),
      rs.array(r.size),
      rs.int(r.crosses_length_to_win),
      rs.array(9)
    )

  private val g = syntax

  def create(game: Game)(implicit session: DBSession = AutoSession): Future[Boolean] = {
    val sql = withSQL(insert.into(Game).namedValues(
      column.nextStep -> game.nextStep,
      column.won -> game.won,
      column.finished -> game.finished,
      column.players -> game.players,
      column.steps -> game.steps,
      column.size -> game.size,
      column.crossesLengthToWin -> game.crossesLengthToWin,
      column.play_field -> game.field
    ))
    Future {
      sql.update().apply() == 1
    }
  }

  def findById(id: Long)(implicit s: DBSession = AutoSession): Future[Option[Game]] = Future {
    val sql = withSQL(
      select
        .from[Game](Game as g)
        .where.eq(g.id, id)
    )
    sql.map(Game(g.resultName)).headOption().apply()
  }
}


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
