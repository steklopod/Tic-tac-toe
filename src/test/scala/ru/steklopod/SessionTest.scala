package ru.steklopod

import org.scalatest.{FunSuite, Matchers}
import ru.steklopod.repositories.{GameDb, PlayerDb}
import spray.json._
import ru.steklopod.util.PlayerJson._

class SessionTest extends FunSuite with Matchers {

  private [this] val sessionValue = "$2a$10$SjInPRoSm"

  test("CREATE by name") {
    GameDb.init()
    PlayerDb.createSession(sessionValue)
  }

  test("IS EXIST?") {
    GameDb.init()
    val isExist = PlayerDb.isSessionExist(sessionValue)

    isExist should be(true)
  }

  test("DELETE by name") {
    GameDb.init()
    PlayerDb.deleteSession(sessionValue)

    PlayerDb.isSessionExist(sessionValue) should be (false)
  }

  test("DELETE old") {
    GameDb.init()
    PlayerDb.deleteOldSessions
  }

  test ("to JSON"){
    val json = Map( "session" -> sessionValue).toJson
    println(json)
  }

}