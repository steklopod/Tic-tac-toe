package ru.steklopod

import org.scalatest.{FunSuite, Matchers}
import ru.steklopod.entities.{Game, Player}
import ru.steklopod.repositories.{DBGameRepository, GameDb}

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.util.{Failure, Success}

class DatabaseTest extends FunSuite with Matchers {

  test("TODO ") {
    GameDb.init()
    DBGameRepository.findAll()
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