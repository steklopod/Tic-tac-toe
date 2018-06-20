package ru.steklopod.entities

import ru.steklopod.util.GameFieldConverter._
import scalikejdbc._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

case class Game(id: Option[Long],
                var nextStep: String,
                won: Option[String],
                finished: Boolean,
                players: String,
                steps: Int,
                size: Vector[Int],
                crossesLengthToWin: Int,
                var fieldPlay: Vector[Vector[Int]]
               ) {
  def this(nextStep: String, won: Option[String], finished: Boolean, players: String, steps: Int, size: Vector[Int], crossesLengthToWin: Int, fieldPlay: Vector[Vector[Int]]) {
    this(Option.empty[Long], nextStep, won, finished, players, steps, size, crossesLengthToWin, fieldPlay)
  }

  def this(nextStep: String, won: Option[String], finished: Boolean, players: String, steps: Int, size: Vector[Int], crossesLengthToWin: Int) {
    this(Option.empty[Long], nextStep, won, finished, players, steps, size, crossesLengthToWin, makeFieldFromSize(size))
  }

  def this(opponent: String, size: Vector[Int], firstStepBy: String, crossesLengthToWin: Int) {
    this(firstStepBy, None, false, firstStepBy + ", " + opponent, 0, size, crossesLengthToWin, makeFieldFromSize(size))
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

  def create(game: Game)(implicit session: DBSession = AutoSession): Game = {
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
    //    Future {
    val genId: Long = sql.updateAndReturnGeneratedKey().apply()
    //    }
    printGame(game)
    game.copy(id = Option(genId))
  }

  def updateFieldAndNextStep(game: Game, gameId: Long): Int = {
    Game.printGame(game)
    DB autoCommit { implicit session =>
      val sql = withSQL(update(Game).set(
        column.fieldPlay -> convertFieldFromVectorToString(game.fieldPlay),
        column.nextStep -> game.nextStep
      )
        .where.eq(column.id, gameId))
      sql.update.apply()
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


  def findAll: List[Game] = DB readOnly { implicit session =>
    val sql = withSQL {
      select.from(Game as g)
    }
      .map(Game(g.resultName))
    sql.list.apply()
  }

  def findAll(max: Int): List[Game] = DB readOnly { implicit session =>
    val limitGames = withSQL {
      select.from(Game as g)
        .limit(max)
    }
      .map(Game(g.resultName))
      .list
    limitGames.apply()
  }

  def findAll(max: Int, skip: Int): List[Game] = DB readOnly { implicit session =>
    val limitGames = withSQL {
      select.from(Game as g)
        .limit(max)
        .offset(skip)
    }
      .map(Game(g.resultName))
      .list
    limitGames.apply()
  }

  def printGame(game: Game): Unit = {
    val players: Array[String] = game.players.split(",")
    println("Player 1: " + players(0).trim())
    println("Player 2: " + players(1).trim())
    println("\n")

    game.fieldPlay
      .foreach(row => println(row.mkString("[", " | ", "]")))
    println("\n")
  }

  @throws(classOf[IllegalArgumentException])
  def makeStep(game: Game, step: List[Int]): Game = {
        require(step.size == 2, "Step must contains only 2 elements")
    val hieight = game.size.head; val width = game.size(1)
    val h = step.head;            val w = step(1)
        require(hieight > h, s"Out of bounds. Please, set Horizontal Row < ${hieight}.")
        require(width > w, s"Out of bounds. Please, set Vertical Row < ${width}.")
    var field = game.fieldPlay
    val players: Array[String] = game.players.split(",").map(_.trim)
        require(field(h)(w) == 0, s"This position is taken. The move is not possible.")
    val indexOfCurrentPlayer = players.indexOf(game.nextStep)
    val row = game.fieldPlay(h).updated(w, indexOfCurrentPlayer + 1)

    val playersReverse = players.reverse
    game.nextStep = playersReverse(indexOfCurrentPlayer)
    game.fieldPlay = field.updated(h, row)
    game
  }

}