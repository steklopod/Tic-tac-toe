package ru.steklopod

import org.scalatest.{FunSuite, Matchers}
import ru.steklopod.entities.Player
import scala.concurrent.ExecutionContext.Implicits.global

class DatabaseTest extends FunSuite with Matchers {

  test("Get existing user from table") {
    import scala.util.{Failure, Success}

    val username = "testName"
    Player.findByName(username).onComplete(
      {
        case Success(value) => println(s"Got the callback, meaning = $value")
        case Failure(e) => println("Ничего не неайдено")
        case b => println(b)
      })
  }

}