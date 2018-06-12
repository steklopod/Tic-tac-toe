package ru.steklopod.util

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import ru.steklopod.entities.Player
import spray.json.{DefaultJsonProtocol, RootJsonFormat}

object PlayerJson extends DefaultJsonProtocol with SprayJsonSupport {
  implicit val playerFormat: RootJsonFormat[Player] = jsonFormat6(Player.apply)
}
