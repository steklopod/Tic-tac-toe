package ru.steklopod.util

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import ru.steklopod.entities.Player
import spray.json._
import com.github.t3hnar.bcrypt._

object PlayerJson extends DefaultJsonProtocol with SprayJsonSupport {

  //    implicit val playerFormat = jsonFormat6(Player.apply)

  implicit object PlayerJsonFormat extends RootJsonFormat[Player] {

    override def read(json: JsValue): Player =
      json.asJsObject.getFields("username", "password") match {
        case Seq(JsString(name), JsString(password)) => new Player(name, password.bcrypt)
        case _ => deserializationError("Player expected")
      }


    def write(p: Player): JsValue = JsObject(
      "username" -> JsString(p.username),
      "online" -> JsBoolean(p.online),
      "wins" -> JsNumber(p.wins),
      "losses" -> JsNumber(p.losses)
    )
  }


  //  implicit val playerFormat = jsonFormat(Player, "id", "username", "password", "online", "wins", "losses")
}
