package ru.steklopod

import org.scalatest.{FunSuite, Matchers}
import ru.steklopod.entities.Game
import ru.steklopod.repositories.{DBGameRepository, GameDb}

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
    var game = new Game("Vasya", Vector(3, 3), "Dima", 3)
    println(game)

    val step = List(0, 1)

    val newField = Game.makeStep(game, step)
    game.fieldPlay = newField
    println("AFTER:")

    println(game)
  }

}