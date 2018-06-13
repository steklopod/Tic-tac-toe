package ru.steklopod

import com.tsukaby.bean_validation_scala.ScalaValidatorFactory
import org.scalatest.FunSuite
import ru.steklopod.entities.Player
import ru.steklopod.util.PlayerJson._
import spray.json._


class PlayerTest extends FunSuite with  DefaultJsonProtocol{
//  val playerRepository: PlayerRepository

  val validator = ScalaValidatorFactory.validator

  test ("Player - to JSON [create]"){
    val source =
      """
    {
     "username": "vasya",
     "password": "c4Gf4g4g"
    }
     """
    val jsonAst = source.parseJson // or JsonParser(source)
    val game = jsonAst.convertTo[Player]
    println(game)
  }


  test ("Session JSON"){
//    case class Session(session: String)
//    implicit val sessionFormat = jsonFormat1(Session)
    val json = Map( "ds" -> "$2a$10$Gkuw6ZCM").toJson
    println(json)
  }






}
