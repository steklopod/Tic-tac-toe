package ru.steklopod.api

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directive0
import akka.http.scaladsl.server.Directives._
import ru.steklopod.entities.Game
import ru.steklopod.repositories.GameRepository
import ru.steklopod.util.MyJsonProtocol._
import spray.json._
import ru.steklopod.repositories.PlayerDb._

import scala.util.{Failure, Success, Try}

trait WithAuth {
  def withAuth: Directive0 = optionalHeaderValueByName("admin")
    .flatMap {
      case Some(_) | None => pass
      case _ => complete(StatusCodes.Unauthorized)
    }
}

trait WithSession {
  def withSession: Directive0 = optionalHeaderValueByName("session")
    .flatMap {
      case Some(uid) if isSessionExist(uid) => pass
      case _ => complete(StatusCodes.Unauthorized -> "Session is finished or not exist in header.")
    }
}

trait GameApi extends WithAuth with WithSession {

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
          //           parameterMap { paramsMap =>
          onSuccess(gameRepository.getGame(id)) {
            case Some(game) => complete(StatusCodes.OK -> JsObject(game.toJson.asJsObject.fields /* ++ Map("params" -> paramsMap.toJson) */))
            case None => complete(StatusCodes.NotFound)
            //              }
          }
        }
      } ~ withSession {
        post {
          path(LongNumber) { id =>
            entity(as[JsValue]) { json =>
              val step = json.asJsObject.fields("step").convertTo[List[Int]]
              onSuccess(gameRepository.getGame(id)) {
                case Some(game) => {
                  var newField = Try(Game.makeStep(game, step).fieldPlay)
                  newField match {
                    case Success(field) => {
                      game.fieldPlay = field
                      Game.updateFieldAndNextStep(game, id)
                      Game.printGame(game)
                      complete(StatusCodes.OK -> JsObject(game.toJson.asJsObject.fields))
                    }
                    case Failure(ex) => complete(StatusCodes.BadRequest -> s"You've made a mistake in request: ${ex.getMessage}")
                  }
                  //TODO - доделать

                }
                case None => complete(StatusCodes.NotFound)
              }
            }
          } ~ entity(as[Game]) { game =>
            complete {
              StatusCodes.OK -> JsObject(gameRepository.createGame(game).toJson.asJsObject.fields)
            }
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