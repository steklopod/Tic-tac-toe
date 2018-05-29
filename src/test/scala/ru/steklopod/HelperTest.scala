package ru.steklopod

import org.scalatest.{FunSuite, Matchers}
import ru.steklopod.entities.Helper
import ru.steklopod.entities.Helper.makeSeqFromStr

import scala.collection.mutable.{ListBuffer, Seq}

class HelperTest extends FunSuite with Matchers {

  test("From Seq[Int] => String and back") {
    val str: String = Helper.makeStringFromSeq(Seq(3, 3, 3))
    print(s" Result String is $str")

    val a: Seq[Int] = Helper.makeSeqFromStr(str)
    print(s"\n Result Seq[Int] is $a")
    a.size should be(3)
  }

  test("Parse game field - Helper test") {
    val gameFieldStr = "0, 0, 1, 0, 0, 1, 0, 0, 1"
    val fieldSeq = Helper.getFieldListFromString(gameFieldStr)
    println(fieldSeq)
    fieldSeq.foreach(println)
  }

  test("Parse game field - full method test") {
    val gameFieldStr = "0, 0, 1, 0, 0, 1, 0, 0, 1"
    val gameFieldSeq: Seq[Int] = makeSeqFromStr(gameFieldStr)
    val fieldSize = makeSeqFromStr(Helper.ThreeByThree.toString)(1) //TODO - изменение размера поля

    gameFieldSeq.size should equal (makeSeqFromStr(Helper.ThreeByThree.toString)(0) * makeSeqFromStr(Helper.ThreeByThree.toString)(1))

    var fieldsSeq = new ListBuffer[Seq[Int]]()

    var from = 0
    for (n <- 1 to fieldSize) {
      var to = fieldSize * n
      val tuple: Seq[Int] = gameFieldSeq.slice(from, to)
      fieldsSeq += tuple
      from = to

      print(s"\n field row $n: " + tuple)
    }

    fieldsSeq.size should equal(fieldSize)
    fieldsSeq(1)(2) should equal(1)

    print("\n" + fieldsSeq)
  }

  test("From ListBuffer[Seq[Int]] => String") {
    val gameFieldStr = "0, 0, 1, 0, 0, 1, 0, 0, 1"
    val fieldSeq = Helper.getFieldListFromString(gameFieldStr)
    val s = Helper.makeFieldsStringFromSeq(fieldSeq)
    print(s)
  }
}
