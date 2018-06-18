package ru.steklopod

import com.tsukaby.bean_validation_scala.ScalaValidatorFactory
import org.scalatest.FunSuite
import ru.steklopod.entities.Player
import ru.steklopod.util.PlayerJson._
import spray.json._


class PlayerTest extends FunSuite with  DefaultJsonProtocol{

  val validator = ScalaValidatorFactory.validator

  test ("Player - to JSON [create]"){
    val source =
      """
    {
     "username": "testName",
     "password": "Test password"
    }
     """
    val jsonAst = source.parseJson // or JsonParser(source)
    val game = jsonAst.convertTo[Player]
    println(game)
  }


  test ("Session JSON"){
    val json = Map( "session" -> "$2a$10$Gkuw6ZCM").toJson
    println(json)
  }



}
