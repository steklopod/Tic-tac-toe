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
  }
}


class JsonTest extends FunSuite with MyJsonProtocol {


  test("JSON") {
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

