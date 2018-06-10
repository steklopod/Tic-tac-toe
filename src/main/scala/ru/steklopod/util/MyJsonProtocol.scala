package ru.steklopod.util

import ru.steklopod.entities.{Game, Player}
import ru.steklopod.util.Helper.getFieldListFromString
import spray.json.{DefaultJsonProtocol, DeserializationException, JsArray, JsBoolean, JsNumber, JsObject, JsString, JsValue, RootJsonFormat, _}


object MyJsonProtocol extends DefaultJsonProtocol {
  implicit val playerFormat: RootJsonFormat[Player] = jsonFormat3(Player.apply)

  //TODO - next_step - Str, won - Str
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
