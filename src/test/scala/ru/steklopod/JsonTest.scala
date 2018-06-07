package ru.steklopod

import org.scalatest.FunSuite
import ru.steklopod.entities.{Game, Helper}
import spray.json.{DefaultJsonProtocol, _}

trait MyJsonProtocol extends DefaultJsonProtocol with NullOptions {

  //  implicit val gameFormat = jsonFormat9(Game.apply)

  implicit val gameFormat = new JsonWriter[Game] {
    def write(g: Game): JsValue = {
      val id: JsValue = g.id match {
        case Some(x) => JsNumber(x)
        case None => JsNull
      }
      val wonz: JsValue = g.won match {
        case Some(x) => JsNumber(x)
        case None => JsNull
      }
      JsObject(
        "id" ->  id,
        "next_step" -> JsNumber(g.nextStep),
        "won" -> wonz,
        "finished" -> JsBoolean(g.finished),
        "players" -> JsString(g.players),
        "steps" -> JsNumber(g.steps),
        "size" -> JsString(g.size),
        "crosses_length_to_win" -> JsNumber(g.crossesLengthToWin),
        "field" -> JsString(g.fieldPlay)
      )
    }
  }
}

class JsonTest extends FunSuite with MyJsonProtocol {

  test("JSON") {
    val game = new Game(1, None, false, "1, 2", 0, Helper.ThreeByThree.toString, 3, "0, 0, 0, 0, 0, 0, 0, 0, 0")
    val marshalled = game.toJson
    println(marshalled)
  }

}

