package ru.steklopod

import org.scalatest.{FunSuite, Matchers}
import ru.steklopod.entities.Player
import ru.steklopod.repositories.GameDb

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Await
import scala.concurrent.duration._
import scala.util.{Failure, Success}

class DatabaseTest extends FunSuite with Matchers {

  test("Get existing user from table") {
    val username = "testName"

    GameDb.init()

    val player = Await.result(Player.findByName(username), 2 second)
    print("Player from Await: " + player)

    Player.findByName(username).onComplete(
      {
        case Success(value) => println(s"Found player: $value")
        case Failure(e) => println("Ничего не неайдено")
      })
  }

}