package ru.steklopod

import org.scalatest.{FunSuite, Matchers}
import ru.steklopod.entities.Game
import ru.steklopod.repositories.{DBGameRepository, GameDb}
import ru.steklopod.util.GameFieldConverter.convertFieldFromVectorToString
import scala.concurrent.duration._

import scala.concurrent.Await

class GameDBTest extends FunSuite with Matchers {

  test("Find all") {
    GameDb.init()
    val allGames = DBGameRepository.findAll()
    println("count of games: " + allGames.size)
    allGames.foreach(println)
  }

  test("Find all with limit") {
    GameDb.init()
    val limit = 2
    val allGames = DBGameRepository.findAll(2, 1)
    println("count of games: " + allGames.size)
    allGames.foreach(println)
  }

  test("Update gameField test") {
    GameDb.init()
    var game = Await.result(Game.findById(1), Duration.Inf).get
//      new Game("Vasya", Vector(3, 3), "Dima", 3)

    println(game.size)

    val step = List(0, 1) //IllegalArgumentException

    var newField = Game.makeStep(game, step)

    game.fieldPlay = newField

    println("AFTER:")
    println(game)

    val fieldAsStr = convertFieldFromVectorToString(newField)
    println(fieldAsStr)

    Game.updateField(game, 1)
  }

}