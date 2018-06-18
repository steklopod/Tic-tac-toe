package ru.steklopod

import com.tsukaby.bean_validation_scala.ScalaValidatorFactory
import org.scalatest.FunSuite
import ru.steklopod.entities.Player
import ru.steklopod.repositories.GameDb
import ru.steklopod.util.PlayerJson._
import spray.json._

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.util.{Failure, Success}

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


  test("Get existing user from table") {
    GameDb.init()
    val username = "testName"

    //так правильно:
    Player.findByName(username).onComplete(
      {
        case Success(value) => println(s"Found player: $value")
        case Failure(e) => println("Ничего не неайдено")
      })

    //так лучше не делать:
    val player = Await.result(Player.findByName(username), 4 second)

    val hasOrNo = player match {
      case Some(s) => "Есть"
      case None => "Нет"
    }
    println(hasOrNo)

    val playerNotExist = Await.result(Player.findByName(username * 2), 2 second)

    println("Player +: " + player)
    println("Player -: " + playerNotExist)
  }


}
