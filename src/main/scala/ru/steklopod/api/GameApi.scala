package ru.steklopod.api

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directive0
import akka.http.scaladsl.server.Directives._
import ru.steklopod.entities.Game
import ru.steklopod.repositories.GameRepository
import spray.json._
import ru.steklopod.util.MyJsonProtocol._

trait WithAuth {
  def withAuth: Directive0 = optionalHeaderValueByName("admin")
    .flatMap {
      case Some(_) | None => pass
      case _ => complete(StatusCodes.Unauthorized)
    }
}

trait GameApi extends WithAuth {

  val gameRepository: GameRepository

  val route =
    pathPrefix("game") {
      get {
        //https://doc.akka.io/docs/akka-http/current/routing-dsl/directives/parameter-directives/parameters.html
        parameters("limit".as[Int] ? 1, "offset".as[Int] ? 0) { (limit, offset) =>
          if (limit <= 0 | offset < 0) complete(StatusCodes.BadRequest -> "Please, make limit > 0 & offset >= 0")
          else {
            lazy val limitGames = gameRepository.findAll(limit, offset)
            complete(StatusCodes.OK -> limitGames.toJson)
          }
        } ~ path(LongNumber) { id =>
          //            parameterMap { paramsMap =>
          onSuccess(gameRepository.getGame(id)) {
            case Some(game) => complete(StatusCodes.OK -> JsObject(game.toJson.asJsObject.fields /* ++ Map("params" -> paramsMap.toJson) */))
            case None => complete(StatusCodes.NotFound)
            //              }
          }
        }
      }
    } ~ post {
      entity(as[Game]) { game =>
        complete {
          StatusCodes.OK -> JsObject(gameRepository.createGame(game).toJson.asJsObject.fields)
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


