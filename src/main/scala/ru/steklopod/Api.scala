package ru.steklopod

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directive0
import akka.http.scaladsl.server.Directives._
import com.tsukaby.bean_validation_scala.ScalaValidatorFactory
import ru.steklopod.entities.{Game, Player}
import ru.steklopod.repositories.{GameRepository, PlayerRepository}
import spray.json._

import scala.concurrent.Await
import scala.concurrent.duration._

trait WithAuth {
  def withAuth: Directive0 = optionalHeaderValueByName("admin")
    .flatMap {
      case Some(_) | None => pass
      case _ => complete(StatusCodes.Unauthorized)
    }
}

trait Api extends WithAuth {

  import ru.steklopod.util.MyJsonProtocol._

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


trait PlayerApi {

  import ru.steklopod.util.PlayerJson._

  val playerRepository: PlayerRepository
  val validator = ScalaValidatorFactory.validator

  val routeUser =
    pathPrefix("user") {
      post {
        pathPrefix("login") {
          entity(as[Player]) { player =>
            complete(StatusCodes.OK -> "ЕСТЬ >>>>>>>>>>")
          }
        } ~
        pathEndOrSingleSlash {
          entity(as[Player]) { player =>
            val violations = validator.validate(player)
            if (violations.nonEmpty) {
              complete(StatusCodes.BadRequest -> "Name must be from 4 to 20 chars.")
            } else {
              val username = player.username
              val answer = Await.result(playerRepository.createPlayer(player), 2 second)
              answer match {
                case ok if ok => complete(StatusCodes.OK -> s"User with name [$username] succesfully created")
                case false => complete(StatusCodes.Conflict -> s"Player `$username` is existing. Please, choose another name.")
              }
            }
          }
        }
      } ~ get {
        parameters("limit".as[Int] ? 1, "offset".as[Int] ? 0) { (limit, offset) =>
          if (limit <= 0 | offset < 0) complete(StatusCodes.BadRequest -> "Please, make limit > 0 & offset >= 0")
          else {
            lazy val limitGames = playerRepository.findAll(limit, offset)
            complete(StatusCodes.OK -> limitGames.toJson)
          }
        } ~ path(Segment) { username =>
          onSuccess(playerRepository.findByName(username)) {
            case Some(player) => complete(StatusCodes.OK -> JsObject(player.toJson.asJsObject.fields))
            case None => complete(StatusCodes.NotFound -> s"Player with name $username isn't exist")
          }
        }
      }
    }

  //case class Session(name: String, value: String){}
  //object Session
  //  val sessionUID = (username.bcrypt + System.currentTimeMillis().toString.bcrypt).bcrypt
  //  complete(StatusCodes.OK -> ("session" -> sessionUID).toJson)

}