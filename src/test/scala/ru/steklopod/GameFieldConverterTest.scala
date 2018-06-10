package ru.steklopod

import org.scalatest.{FunSuite, Matchers}
import ru.steklopod.util.GameFieldConverter._


class GameFieldConverterTest extends FunSuite with Matchers {

  test("Make `field` when `size` is known:  Vector[Int] => Vector[Vector[Int]]") {
    val fieldSize = Vector(3, 3)
      val width = fieldSize(0)
      val height = fieldSize(1)

    val fieldVector: Vector[Vector[Int]] = Vector.fill(height, width)(0)

    fieldVector.size should be(height)
    fieldVector(0).size should be(width)

      println(fieldVector)
      fieldVector.foreach(println)
  }

  test("Converting `field`: Vector[Vector[Int]] <=> String") {
    val fieldVector: Vector[Vector[Int]] = makeFieldFromSize(Vector(3, 3))

      val fieldStr: String = convertFieldFromVectorToString(fieldVector)

    val fieldVector_Again: Vector[Vector[Int]] = convertFieldFromStringToVector(fieldStr)

    fieldVector should be(fieldVector_Again)
      
      println(fieldVector_Again)
  }

}
