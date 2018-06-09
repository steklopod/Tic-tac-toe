package ru.steklopod

import org.scalatest.FunSuite
import ru.steklopod.entities.Helper._
import ru.steklopod.entities.{Game, Helper}
import ru.steklopod.repositories.{DBGameRepository, GameDb}
import spray.json.{DefaultJsonProtocol, _}

import scala.concurrent.Await
import scala.concurrent.duration._


trait MyJsonProtocol extends DefaultJsonProtocol {
  implicit val gameFormat = new JsonWriter[Game] {
    def write(g: Game): JsValue = {
      JsObject(
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
    }

    //TODO
    def read(value: JsValue) = {
      value.asJsObject.getFields("opponent", "size", "first_step_by", "crosses_length_to_win") match {
        case Seq(JsString(opponent), JsArray(size), JsString(firstStepBy), JsNumber(crossesLengthToWin)) =>
          new Game(firstStepBy, None, false, "Robot, " + opponent, 0, Helper.ThreeByThree.toString, 3, "0, 0, 0, 0, 0, 0, 0, 0, 0")
        case _ => throw new DeserializationException("Game expected")
      }
    }

  }
}


class JsonTest extends FunSuite with MyJsonProtocol {
  //TODO

  test("JSON read"){
    val json = """
              {
      "opponent": "vasya",
      "size": [3, 3],
      "first_step_by": "vasya",
      "crosses_length_to_win": 3
    }
               """.toJson

    val game = json.convertTo[Game]

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

