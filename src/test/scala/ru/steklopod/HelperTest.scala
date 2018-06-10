package ru.steklopod

import org.scalatest.{FunSuite, Matchers}
import ru.steklopod.util.Helper
import ru.steklopod.util.Helper.makeSeqFromStr

import scala.collection.mutable.ListBuffer


class HelperTest extends FunSuite with Matchers {

  test("Vector -> string") {
    val sizeVector = Vector(3, 4)
  }

  test("From Seq[Int] => String and back") {
    val str: String = Helper.makeStringFromSeq(Seq(3, 3, 3))
    print(s" Result String is $str")

    val a: Seq[Int] = Helper.makeSeqFromStr(str)
    print(s"\n Result Seq[Int] is $a")
    a.size should be(3)
  }


  test("MakeField from `size` field") {
    val fieldSize = Vector(3, 3)
    val width = fieldSize(0)
    val height = fieldSize(1)
    val v: Vector[Vector[Int]] = Vector.fill(height, width)(0)

    v.size should be(height)
    v(0).size should be(width)

    println(v)
    v.foreach(println)
  }

  test("Make `field`: String from Vector[Vector[Int]] ") {
    val fieldVector: Vector[Vector[Int]] = Helper.makeFieldFromSize(Vector(3, 3))
    val str: String = Helper.makeFieldsStringFromVector(fieldVector)
    println(str)
  }

  test("Make `field`: Vector[Vector[Int]] from String") {
        val fieldVector: Vector[Vector[Int]] = Helper.makeFieldFromSize(Vector(3, 3))
        val str:String = Helper.makeFieldsStringFromVector(fieldVector)

        println(str)
  }



  test("String -> Seq[Seq[Int]]") {
    val gameFieldStr = "0, 0, 1, 0, 0, 1, 0, 0, 1"
    val fieldSeq: Seq[Seq[Int]] = Helper.getFieldListFromString(gameFieldStr)
    println(fieldSeq)
    fieldSeq.foreach(println)
  }


}
