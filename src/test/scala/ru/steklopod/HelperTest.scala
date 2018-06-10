package ru.steklopod

import org.scalatest.{FunSuite, Matchers}
import ru.steklopod.util.Helper
import ru.steklopod.util.Helper.makeSeqFromStr
import spray.json._
import ru.steklopod.util.MyJsonProtocol._

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
    val str:String = Helper.makeFieldsStringFromVector(fieldVector)
    println(str)
  }


  test("Parse game field - full method test") {
    val gameFieldStr = "0, 0, 1, 0, 0, 1, 0, 0, 1"
    val gameFieldSeq: Seq[Int] = makeSeqFromStr(gameFieldStr)
    val fieldSize = Vector(3, 3) //TODO - изменение размера поля

    //    gameFieldSeq.size should equal(makeSeqFromStr(Helper.ThreeByThree.toString)(0) * makeSeqFromStr(Helper.ThreeByThree.toString)(1))

    var fieldsSeq = new ListBuffer[Seq[Int]]()

    var from = 0
    val width = fieldSize(0)
    for (n <- 1 to width) {
      var to = width * n
      val tuple: Seq[Int] = gameFieldSeq.slice(from, to)
      fieldsSeq += tuple
      from = to

      print(s"\n field row $n: " + tuple)
    }

    //    fieldsSeq.size should equal(fieldSize)
    //    fieldsSeq(1)(2) should equal(1)

    print("\n" + fieldsSeq)
  }


  test("String -> Seq[Seq[Int]]") {
    val gameFieldStr = "0, 0, 1, 0, 0, 1, 0, 0, 1"
    val fieldSeq: Seq[Seq[Int]] = Helper.getFieldListFromString(gameFieldStr)
    println(fieldSeq)
    fieldSeq.foreach(println)
  }


}
