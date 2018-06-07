package ru.steklopod

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import org.scalatest.FunSuite
import ru.steklopod.entities.{Game, Helper}
import spray.json.{DefaultJsonProtocol, _}

trait MyJsonProtocol extends DefaultJsonProtocol  with SprayJsonSupport with NullOptions{

  implicit val gameFormat = jsonFormat9(Game.apply)

  /* TODO - не рабочий
    implicit val gameFormat = new JsonWriter[Game] {
      def write(g: Game): JsValue = {
        JsObject(
          "id" -> LongJsonFormat(g.id),
          "next_step" -> IntJsonFormat(g.nextStep),
          "won" -> IntJsonFormat(g.won),
          "finished" -> JsBoolean(g.finished),
          "players" -> JsString(g.players),
          "steps" -> IntJsonFormat(g.steps),
          "size" -> JsString(g.size),
          "crosses_length_to_win" -> IntJsonFormat(g.crossesLengthToWin),
          "field" -> JsString(g.fieldPlay)
        )
      }
    }
    */
}

class JsonTest extends FunSuite with MyJsonProtocol{

  test("JSON") {
    val game = new Game( 1, None, false, "1, 2", 0, Helper.ThreeByThree.toString, 3, "0, 0, 0, 0, 0, 0, 0, 0, 0")
    val marshalled = game.toJson
    println(marshalled)
  }

}

