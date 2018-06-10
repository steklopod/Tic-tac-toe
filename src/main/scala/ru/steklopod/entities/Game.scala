package ru.steklopod.entities

import ru.steklopod.util.GameFieldConverter._
import scalikejdbc._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

final case class Game(id: Option[Long],
                      nextStep: String,
                      won: Option[String],
                      finished: Boolean,
                      players: String,
                      steps: Int,
                      size: Vector[Int],
                      crossesLengthToWin: Int,
                      fieldPlay: Vector[Vector[Int]]
                     ) {
  def this(nextStep: String, won: Option[String], finished: Boolean, players: String, steps: Int, size: Vector[Int], crossesLengthToWin: Int, fieldPlay: Vector[Vector[Int]]) {
    this(Option.empty[Long], nextStep, won, finished, players, steps, size, crossesLengthToWin, fieldPlay)
  }

  def this(nextStep: String, won: Option[String], finished: Boolean, players: String, steps: Int, size: Vector[Int], crossesLengthToWin: Int) {
    this(Option.empty[Long], nextStep, won, finished, players, steps, size, crossesLengthToWin, makeFieldFromSize(size))
  }

  def this(opponent: String, size: Vector[Int], firstStepBy: String, crossesLengthToWin: Int) {
    this(firstStepBy, None, false, "Robot, " + opponent, 0, size, crossesLengthToWin, makeFieldFromSize(size))
  }
}

object Game extends SQLSyntaxSupport[Game] {

  override val tableName = "game"

  def apply(r: ResultName[Game])(rs: WrappedResultSet) =
    new Game(
      rs.longOpt(r.id),
      rs.string(r.nextStep),
      rs.stringOpt(r.won),
      rs.boolean(r.finished),
      rs.string(r.players),
      rs.int(r.steps),
        convertSizeFromStrToVector(rs.string(r.size)),
      rs.int(r.crossesLengthToWin),
        convertFieldFromStringToVector(rs.string(r.fieldPlay))
    )

  private val g = syntax

  def create(game: Game)(implicit session: DBSession = AutoSession): Future[Boolean] = {
    val sql = withSQL(insert.into(Game).namedValues(
      column.nextStep -> game.nextStep,
      column.won -> game.won,
      column.finished -> game.finished,
      column.players -> game.players,
      column.steps -> game.steps,
        column.size -> convertFieldFromVectorToStr(game.size),
      column.crossesLengthToWin -> game.crossesLengthToWin,
        column.fieldPlay -> convertFieldFromVectorToString(makeFieldFromSize(game.size))
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