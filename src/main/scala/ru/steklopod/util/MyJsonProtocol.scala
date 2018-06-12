package ru.steklopod.util

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import ru.steklopod.entities.{Game, Player}
import spray.json.{DefaultJsonProtocol, DeserializationException, JsArray, JsBoolean, JsNumber, JsObject, JsString, JsValue, RootJsonFormat, _}


object MyJsonProtocol extends DefaultJsonProtocol with SprayJsonSupport {
  implicit val playerFormat: RootJsonFormat[Player] = jsonFormat6(Player.apply)

  implicit object GameJsonFormat extends RootJsonFormat[Game] {
    def write(g: Game) = JsObject(
      "id" -> g.id.toJson,
      "next_step" -> JsString(g.nextStep),
      "won" -> g.won.toJson,
      "finished" -> JsBoolean(g.finished),
      "players" -> JsString(g.players),
      "steps" -> JsNumber(g.steps),
      "size" -> JsArray(g.size.map(_.toJson)),
      "crosses_length_to_win" -> JsNumber(g.crossesLengthToWin),
      "field" -> JsArray(g.fieldPlay.map(_.toJson))
    )

    def read(value: JsValue): Game = {
      value.asJsObject.getFields("opponent", "size", "first_step_by", "crosses_length_to_win")
      match {
        case Seq(JsString(opponent), JsArray(size), JsString(firstStepBy), JsNumber(crossesLengthToWin)) =>
          new Game(
            opponent,
            size.map(_.convertTo[Int]),
            firstStepBy,
            crossesLengthToWin.toInt)
        case other => throw new DeserializationException("Game expected")
      }
    }
  }
}
