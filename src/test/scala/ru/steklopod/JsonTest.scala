package ru.steklopod

import org.scalatest.FunSuite
import ru.steklopod.entities.Game
import ru.steklopod.repositories.{DBGameRepository, GameDb}
import ru.steklopod.util.MyJsonProtocol._
import spray.json._

import scala.concurrent.Await
import scala.concurrent.duration._

class JsonTest extends FunSuite {
  test("JSON - read") {
    val source =
      """
    {
      "opponent": "vasya",
      "size": [3,3],
      "first_step_by": "vasya",
      "crosses_length_to_win": 3
    }
     """
    val jsonAst = source.parseJson // or JsonParser(source)
    val game = jsonAst.convertTo[Game]
    println(game)
  }

  test("JSON - write") {
    GameDb.init()

    val sizeOfField = Vector(3, 3)
    val game = new Game("Vasya", None, false, "Vasya, Nagibator", 0, sizeOfField, 3)

    val marshalled = game.toJson
      println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>NEW")
      println(marshalled)
      println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>")

    val gameFromDB = Await.result(DBGameRepository.getGame(1L), 2 second).get
    val marshalledFromDb = gameFromDB.toJson
      println("\n<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<DB")
      println(marshalledFromDb)
      println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<")

  }
}


