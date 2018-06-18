package ru.steklopod

import org.scalatest.{FunSuite, Matchers}
import ru.steklopod.repositories.{GameDb, PlayerDb}
import spray.json._
import ru.steklopod.util.PlayerJson._

class SessionTest extends FunSuite with Matchers {

  test("Session is exist") {
    GameDb.init()
    val isExist = PlayerDb.isSessionExist("b")
    println(">>>Has such session: " + isExist)
  }

  test("Session CREATE") {
    GameDb.init()
    PlayerDb.createSession("$2a$10$SjInPRoSm3iSItFKZA1uBuUyCWfcx6ahbxdHAvOVt/rBOobWOKl66")
  }

  test("Session DELETE") {
    GameDb.init()
    PlayerDb.deleteSession("$2a$10$SjInPRoSm3iSItFKZA1uBuUyCWfcx6ahbxdHAvOVt/rBOobWOKl66")
  }

  test("Delete old sessions") {
    GameDb.init()
    val isExist = PlayerDb.deleteOldSessions
  }

  test ("Session JSON"){
    val json = Map( "session" -> "$2a$10$Gkuw6ZCM").toJson
    println(json)
  }

}