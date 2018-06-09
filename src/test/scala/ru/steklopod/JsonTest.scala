package ru.steklopod

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import org.scalatest.FunSuite
import ru.steklopod.entities.Helper._
import ru.steklopod.entities.{Game, Helper}
import ru.steklopod.repositories.{DBGameRepository, GameDb}
import spray.json._

import scala.concurrent.Await
import scala.concurrent.duration._


object MyJsonProtocol_Test extends DefaultJsonProtocol {

  implicit object GameJsonFormat extends RootJsonFormat[Game] {
    def write(g: Game) = JsObject(
      "id" -> g.id.toJson,
      "next_step" -> JsString(g.nextStep),
      "won" -> g.won.toJson,
      "finished" -> JsBoolean(g.finished),
      "players" -> JsString(g.players),
      "steps" -> JsNumber(g.steps),
      "size" -> JsString(g.size),
      "crosses_length_to_win" -> JsNumber(g.crossesLengthToWin),
      "field" -> getFieldListFromString(g.fieldPlay).toJson
    )

    def read(value: JsValue): Game = {
      value.asJsObject.getFields("opponent", "size", "first_step_by", "crosses_length_to_win")
      match {
        case Seq(JsString(opponent), JsString(size), JsString(firstStepBy), JsNumber(crossesLengthToWin)) =>
          new Game(opponent, firstStepBy, size, crossesLengthToWin.toInt)
        case other => throw new DeserializationException("Game expected")
      }
    }
  }

}


class JsonTest extends FunSuite with SprayJsonSupport {

  import MyJsonProtocol_Test._

  test("JSON read") {
    val source =
      """
    {
      "opponent": "vasya",
      "size": "three",
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


