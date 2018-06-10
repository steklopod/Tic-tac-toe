package ru.steklopod

import spray.json._

case class SuperClass(omfg: Option[String])
trait SupperClassJsonSupport  extends DefaultJsonProtocol {
  implicit val gameFormat = new JsonWriter[SuperClass] {
    def write(g: SuperClass): JsValue = {
      JsObject(
        "omfg" -> g.omfg.toJson
      )
    }
  }
}

object Main extends App with SupperClassJsonSupport {
  println(SuperClass(Some("1234")).toJson)
}