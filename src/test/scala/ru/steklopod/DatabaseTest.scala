package ru.steklopod

import org.scalatest.{FunSuite, Matchers}
import ru.steklopod.entities.Player
import ru.steklopod.repositories.{DBGameRepository, GameDb, PlayerDb}

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.util.{Failure, Success}

class DatabaseTest extends FunSuite with Matchers {

  test("Find all ") {
    GameDb.init()
    val allGames = DBGameRepository.findAll()
    println("count of games: " + allGames.size)
    allGames.foreach(println)
  }
  test("Session ") {
    GameDb.init()

    val isExist = PlayerDb.isSessionExist("edfefefe")
    println("Has such session: " + isExist)
  }

  test("Find all limit") {
    GameDb.init()
    val limit = 2
    val allGames = DBGameRepository.findAll(2, 1)
    println("count of games: " + allGames.size)
    allGames.foreach(println)
  }

  test("Get existing user from table") {
    GameDb.init()
    val username = "testName"

    //так правильно:
    Player.findByName(username).onComplete(
      {
        case Success(value) => println(s"Found player: $value")
        case Failure(e) => println("Ничего не неайдено")
      })

    //так лучше не делать:
    val player = Await.result(Player.findByName(username), 2 second)

    val hasOrNo = player match {
      case Some(s) => "Есть"
      case None => "Нет"
    }
    println(hasOrNo)

    val playerNotExist = Await.result(Player.findByName(username * 2), 2 second)

    println("Player +: " + player)
    println("Player -: " + playerNotExist)
  }

}