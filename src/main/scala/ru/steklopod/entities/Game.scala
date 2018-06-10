package ru.steklopod.entities

import ru.steklopod.util.Helper
//import ru.steklopod.util.Helper._
import scalikejdbc._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

//TODO - next_step - Str, won - Str
final case class Game(id: Option[Long],
                      nextStep: String,
                      won: Option[String],
                      finished: Boolean,
                      players: String,
                      steps: Int,
                      size: Vector[Int],
                      crossesLengthToWin: Int,
                      fieldPlay: String
                     ) {
  def this(nextStep: String, won: Option[String], finished: Boolean, players: String, steps: Int, size: Vector[Int], crossesLengthToWin: Int, fieldPlay: String) {
    this(Option.empty[Long], nextStep, won, finished, players, steps, size, crossesLengthToWin, fieldPlay)
  }

  //  TODO - конструктор для POST
  def this(opponent: String, size: Vector[Int], firstStepBy: String, crossesLengthToWin: Int) {
    this(firstStepBy, None, false, "Robot, " + opponent, 0,
      Vector(3, 4),
      crossesLengthToWin,
      "0, 0, 0, 0, 0, 0, 0, 0, 0")
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
      Helper.strToVector(rs.string(r.size)), //TODO
      rs.int(r.crossesLengthToWin),
      rs.string(r.fieldPlay)
    )

  private val g = syntax

  def create(game: Game)(implicit session: DBSession = AutoSession): Future[Boolean] = {
    val sql = withSQL(insert.into(Game).namedValues(
      column.nextStep -> game.nextStep,
      column.won -> game.won,
      column.finished -> game.finished,
      column.players -> game.players,
      column.steps -> game.steps,
      column.size -> Helper.vectorToStr(game.size),
      column.crossesLengthToWin -> game.crossesLengthToWin,
      column.fieldPlay -> game.fieldPlay
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