package ru.steklopod

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directive0
import akka.http.scaladsl.server.Directives._
import spray.json._

import scala.concurrent.ExecutionContext.Implicits.global

trait TeapotJsonSupport extends DefaultJsonProtocol with SprayJsonSupport {
  implicit val colorFormat = new JsonFormat[Color] {
    override def read(json: JsValue): Color = Color.fromString(json.convertTo[String])

    override def write(obj: Color): JsValue = JsString(obj.toString)
  }
  implicit val teapotFormat = jsonFormat6(Teapot.apply)
}

trait WithAuth {
  def withAuth: Directive0 = optionalHeaderValueByName("auth").flatMap {
    case Some(k) if k == "123" => pass
    case _ => complete(StatusCodes.Unauthorized)
  }
}


trait Api extends TeapotJsonSupport with WithAuth {
  val teapotRepository: TeapotRepository
  val route =
    pathPrefix("teapot") {
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
        entity(as[Teapot]) { teapot =>
          complete {
            teapotRepository.createTeapot(teapot).map(_ => StatusCodes.OK)
          }
        }
      }
//    }
}
