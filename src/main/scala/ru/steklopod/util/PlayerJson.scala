package ru.steklopod.util

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import ru.steklopod.entities.Player
import spray.json._

object PlayerJson extends DefaultJsonProtocol with SprayJsonSupport {
    implicit val playerFormat = jsonFormat6(Player.apply)
//  implicit val playerFormat = jsonFormat(Player, "id", "username", "password", "online", "wins", "losses")
}
