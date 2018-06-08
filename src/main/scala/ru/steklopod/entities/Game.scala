package ru.steklopod.entities

import scalikejdbc._

import scala.collection.mutable.ListBuffer
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

final case class Game(id: Option[Long],
                      nextStep: String,
                      won: Option[Int],
                      finished: Boolean,
                      players: String,
                      steps: Int,
                      size: String,
                      crossesLengthToWin: Int,
                      fieldPlay: String
                     ) {
  def this(nextStep: String, won: Option[Int], finished: Boolean, players: String, steps: Int, size: String, crossesLengthToWin: Int, fieldPlay: String) {
    this(Option.empty[Long], nextStep, won, finished, players, steps, size, crossesLengthToWin, fieldPlay)

//  TODO - конструктор для POST

  }
}

object Game extends SQLSyntaxSupport[Game] {

  override val tableName = "game"

  def apply(r: ResultName[Game])(rs: WrappedResultSet) =
    new Game(
      rs.longOpt(r.id),
      rs.string(r.nextStep),
      rs.intOpt(r.won),
      rs.boolean(r.finished),
      rs.string(r.players),
      rs.int(r.steps),
      rs.string(r.size),
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
      column.size -> game.size,
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


sealed trait Helper


object Helper {
  final case object ThreeByThree extends Helper {
    override def toString: String = "3, 3"
  }

  final case object ForByFour extends Helper {
    override def toString: String = "4, 4"
  }

  def makeSeqFromStr(s: String): Seq[Int] = {
    s.split(",").map(_.trim.toInt)
  }

  def makeStringFromSeq(seq: Seq[Int]): String = {
    seq.mkString(", ")
  }

  def getFieldListFromString(gameFieldStr: String): Seq[Seq[Int]] = {
    val gameFieldSeq: Seq[Int] = makeSeqFromStr(gameFieldStr)

    val fieldHigh = makeSeqFromStr(Helper.ThreeByThree.toString)(1) //TODO - изменение размера поля
    require(gameFieldSeq.size == 9, "Fiield size must be 3:3. Sorry.")

    val fieldsSeq = new ListBuffer[Seq[Int]]()
    var from = 0
    for (n <- 1 to fieldHigh) {
      var to = fieldHigh * n
      val tuple = gameFieldSeq.slice(from, to)
      from = to
      fieldsSeq += tuple
    }
    fieldsSeq
  }

  def makeFieldsStringFromSeq(fieldList: Seq[Seq[Int]]): String = {
    fieldList.flatten.mkString(", ")
  }

  def fromString(s: String): Helper = s match {
    case "3, 3" => Helper.ThreeByThree
//  case "4, 4" => Helper.ForByFour
    case c => throw new IllegalArgumentException(s"There is no such size as $c yet. Sorry :-(")
  }

}
