package ru.steklopod

import org.scalatest.{FunSuite, Matchers}
import ru.steklopod.repositories.{DBGameRepository, GameDb}

class GameDBTest extends FunSuite with Matchers {

  test("Find all") {
    GameDb.init()
    val allGames = DBGameRepository.findAll()
    println("count of games: " + allGames.size)
    allGames.foreach(println)
  }


  test("Find all with limit") {
    GameDb.init()
    val limit = 2
    val allGames = DBGameRepository.findAll(2, 1)
    println("count of games: " + allGames.size)
    allGames.foreach(println)
  }


}