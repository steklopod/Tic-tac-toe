package ru.steklopod

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directive0
import akka.http.scaladsl.server.Directives._
import spray.json._

import scala.concurrent.ExecutionContext.Implicits.global

trait GameJsonSupport extends DefaultJsonProtocol with SprayJsonSupport {
  implicit val colorFormat = new JsonFormat[Helper] {
    override def read(json: JsValue): Helper = Helper.fromString(json.convertTo[String])

    override def write(obj: Helper): JsValue = JsString(obj.toString)
  }
  implicit val teapotFormat = jsonFormat9(Game.apply)
}

//Todo
trait WithAuth {
  def withAuth: Directive0 = optionalHeaderValueByName("auth").flatMap {
    case Some(k) if k == "123" => pass
    case _ => complete(StatusCodes.Unauthorized)
  }
}


trait Api extends GameJsonSupport with WithAuth {
  val teapotRepository: TeapotRepository
  val route =
    pathPrefix("game") {
      path(LongNumber) { id =>
//        withAuth {
          get {
            parameterMap { paramsMap =>
              onSuccess(teapotRepository.getTeapot(id)) {
                case Some(teapot) =>
                  complete(StatusCodes.ImATeapot -> JsObject(teapot.toJson.asJsObject.fields ++ Map("params" -> paramsMap.toJson)))
                case None =>
                  complete(StatusCodes.NotFound)
              }
            }
          }
        }
      } ~ post {
        entity(as[Game]) { teapot =>
          complete {
            teapotRepository.createTeapot(teapot).map(_ => StatusCodes.OK)
          }
        }
      }
//    }
}
