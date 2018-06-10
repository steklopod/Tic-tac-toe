package ru.steklopod

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directive0
import akka.http.scaladsl.server.Directives._
import com.tsukaby.bean_validation_scala.ScalaValidatorFactory
import ru.steklopod.entities.util.Helper
import ru.steklopod.entities.util.Helper.getFieldListFromString
import ru.steklopod.entities.{Game, Player}
import ru.steklopod.repositories.{GameRepository, PlayerRepository}
import spray.json._

import scala.concurrent.Await
import scala.concurrent.duration._


trait GameJsonSupport extends DefaultJsonProtocol with SprayJsonSupport {

  implicit val fieldFormat = new JsonFormat[Helper] {
    override def read(json: JsValue): Helper = Helper.fromString(json.convertTo[String])

    override def write(obj: Helper): JsValue = JsString(obj.toString)
  }
  implicit val gameFormat = jsonFormat9(Game.apply)
  implicit val playerFormat = jsonFormat3(Player.apply)
}


trait MyJsonProtocol extends DefaultJsonProtocol with SprayJsonSupport {
  implicit val playerFormat: RootJsonFormat[Player] = jsonFormat3(Player.apply)

//  implicit val fieldFormat = new JsonFormat[Helper] {
//    override def read(json: JsValue): Helper = Helper.fromString(json.convertTo[String])
//    override def write(obj: Helper): JsValue = JsString(obj.toString)
//  }

  implicit val gameFormat = new JsonWriter[Game] {
    override def write(g: Game): JsValue = {
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
/*
    //TODO - read JSON
    override def read(value: JsValue):Game = {
      value.asJsObject.getFields("opponent", "size", "first_step_by", "crosses_length_to_win") match {
        case Seq(JsString(opponent), JsArray(size), JsString(firstStepBy), JsNumber(crossesLengthToWin)) =>
          new Game(firstStepBy, None, false, "Robot, " + opponent, 0, Helper.ThreeByThree.toString, 3, "0, 0, 0, 0, 0, 0, 0, 0, 0")
        case _ => throw new DeserializationException("Game expected")
      }
    }*/
  }

}


trait WithAuth {
  def withAuth: Directive0 = optionalHeaderValueByName("admin")
    .flatMap {
      case Some(_) | None => pass
      case _ => complete(StatusCodes.Unauthorized)
    }
}


trait Api extends MyJsonProtocol with WithAuth {
  val gameRepository: GameRepository
  val playerRepository: PlayerRepository
  val validator = ScalaValidatorFactory.validator

  val route =
    pathPrefix("game") {
      path(LongNumber) { id =>
        get {
          parameterMap { paramsMap =>
            onSuccess(gameRepository.getGame(id)) {
              case Some(game) =>
                complete(StatusCodes.OK -> JsObject(game.toJson.asJsObject.fields /* ++ Map("params" -> paramsMap.toJson) */))
              case None =>
                complete(StatusCodes.NotFound)
            }
          }
        }
      }
    } /*~ post {
      entity(as[Game]) { game =>
        complete {
          gameRepository.createGame(game).map(_ => StatusCodes.OK)
        }
      }
    } //TODO - READ JSON
*/

  val routeUser =
    pathPrefix("user") {
      post {
        entity(as[Player]) {
          player =>
            val violations = validator.validate(player)
            if (violations.nonEmpty) {
              complete(StatusCodes.BadRequest -> "Name must be from 4 to 20 chars.")
            } else {
              val username = player.username
              val answer = Await.result(playerRepository.createPlayer(player), 2 second)
              answer match {
                case ok if ok => complete(StatusCodes.OK -> s"User with name $username succesfully created")
                case false => complete(StatusCodes.Conflict -> s"Player `$username` is existing. Please, choose another name.")
              }
            }
        }
      }
    } ~ get {
      path(Segment) { username =>
        parameterMap { paramsMap =>
          onSuccess(playerRepository.findByName(username)) {
            case Some(player) =>
              complete(StatusCodes.OK -> JsObject(player.toJson.asJsObject.fields ++ Map("params" -> paramsMap.toJson)))
            case None =>
              complete(StatusCodes.NotFound -> s"Player with name $username isn't exist")
          }
        }
      }
    }

  val routeDebug =
    pathPrefix("debug") {
      path("reset") {
        withAuth {
          post {
            complete(StatusCodes.OK -> "Data succesfully deleted from tables.")
          }
        }
      }
    }
}