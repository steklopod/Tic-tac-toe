package ru.steklopod

import org.scalatest.{FunSuite, Matchers}
import ru.steklopod.entities.Game
import ru.steklopod.repositories.{DBGameRepository, GameDb}

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.util.{Failure, Success, Try}

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


    val step = List(0, 1) //IllegalArgumentException

    var newGame = Try(Game.makeStep(game, step))

    newGame match {
      case Success(gam) => {
        println("/// AFTER:")
        println(gam)
        Game.updateFieldAndNextStep(gam, 1)
      }
      case Failure(ex) => println(s"Problem: ${ex.getMessage}")
    }

  }

}