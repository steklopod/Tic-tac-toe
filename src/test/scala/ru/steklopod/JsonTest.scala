package ru.steklopod

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import org.scalatest.FunSuite
import ru.steklopod.entities.util.Helper._
import ru.steklopod.entities.Game
import ru.steklopod.entities.util.Helper
import ru.steklopod.repositories.{DBGameRepository, GameDb}
import spray.json._
import scala.concurrent.Await
import scala.concurrent.duration._
import ru.steklopod.entities.util.MyJsonProtocol._


class JsonTest extends FunSuite with SprayJsonSupport {

  test("JSON read") {
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

  test("JSON post") {
    GameDb.init()
    val game = new Game("Vasya", None, false, "1, 2", 0, Helper.ThreeByThree.toString, 3, "0, 0, 0, 0, 0, 0, 0, 0, 0")
    val gameFromDB = Await.result(DBGameRepository.getGame(1L), 2 second).get

    val marshalled = game.toJson
    println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>NEW")
    println(marshalled)
    println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>")

    val marshalledFromDb = gameFromDB.toJson
    println("\n<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<DB")
    println(marshalledFromDb)
    println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<")
  }
}


