package ru.steklopod.api

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives.{as, complete, entity, get, onSuccess, parameters, path, pathEndOrSingleSlash, pathPrefix, post, _}
import com.github.t3hnar.bcrypt._
import com.tsukaby.bean_validation_scala.ScalaValidatorFactory
import ru.steklopod.entities.Player
import ru.steklopod.repositories.{PlayerDb, PlayerRepository}
import ru.steklopod.util.PlayerJson._
import spray.json._

import scala.concurrent.Await
import scala.concurrent.duration._

trait PlayerApi extends WithSession {
  val playerRepository: PlayerRepository
  val validator = ScalaValidatorFactory.validator

  val routeUser =
    pathPrefix("user") {
      post {
        entity(as[Player]) { player =>
          val violations = validator.validate(player)
          if (violations.nonEmpty) complete(StatusCodes.BadRequest -> "Name must be from 4 to 20 chars.")
          val username = player.username

          pathPrefix("login") {
            onSuccess(playerRepository.findByName(username)) {
              case Some(playerFromDB) => {
                val isSamePswrd = player.password.get.isBcrypted(playerFromDB.password.get)
                if (isSamePswrd) {
                  val sessionUID = (username + System.currentTimeMillis().toString).bcrypt
                  PlayerDb.createSession(sessionUID)
                  complete(StatusCodes.OK -> Map("session" -> sessionUID).toJson)
                } else {
                  complete(StatusCodes.Forbidden -> s"Wrong password. Try again.")
                }
              }
              case None => complete(StatusCodes.Forbidden -> s"Player with name [$username] isn't exist")
            }
          } ~ pathPrefix("logout") {
            withSession {
              headerValueByName("session") { sessionValue =>
                onSuccess(playerRepository.findByName(username)) {
                  case Some(playerFromDB) => {
                    val hasSuchPswrd = player.password.get.isBcrypted(playerFromDB.password.get)
                    if (hasSuchPswrd) {
                      PlayerDb.deleteSession(sessionValue)
                      complete(StatusCodes.OK -> "Logout is successful")
                    } else {
                      complete(StatusCodes.Unauthorized -> s"Wrong password. Try again.")
                    }
                  }
                  case None => complete(StatusCodes.Forbidden -> s"Player with name [$username] isn't exist")
                }
              }
            }
          } ~
            pathEndOrSingleSlash {
              val answer = Await.result(playerRepository.createPlayer(player), 5 second)
              answer match {
                case ok if ok => complete(StatusCodes.OK -> s"User with name [$username] succesfully created")
                case false => complete(StatusCodes.Conflict -> s"Player `$username` is existing. Please, choose another name.")
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
}
