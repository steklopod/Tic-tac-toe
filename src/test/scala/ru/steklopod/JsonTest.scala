package ru.steklopod

import org.scalatest.FunSuite
import ru.steklopod.entities.{Game, Helper}
import spray.json._

class JsonTest extends FunSuite with GameJsonSupport {

  //  implicit val fooFormat = new JsonFormat[Game] lazyFormat(jsonFormat(Game, "id", "next_step", "won", "finished", "players", "steps", "size", "crosses_length_to_win", "field"))

  test("JSON") {
    val game = new Game(1, Option.empty, false, "1, 2", 0, Helper.ThreeByThree.toString, 3, "0, 0, 0, 0, 0, 0, 0, 0, 0")
    val marshalled = JsObject(game.toJson.asJsObject.fields)
//    val marshalled = game.toJson

    println(marshalled)
  }

}
