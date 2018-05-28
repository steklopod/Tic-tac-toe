package ru.steklopod

import org.scalatest.{FunSuite, Matchers}

class GameApiSpec extends FunSuite with Matchers {

  test("From Seq[Int] => String and back") {
    val str: String = Helper.makeArrayStringFromSeq(Seq(3, 3, 3))
    print(s" Result String is $str")

    val a: Seq[Int] = Helper.makeSeqFromStr(str)
    print(s"\n Result Seq[Int] is $a")
    a.size should be(3)
  }

  test("Parse game field") {
    val gameFieldStr = "0, 0, 1, 0, 0, 1, 0, 0, 1"
    val fieldSeq = Helper.getFieldSeqFromString(gameFieldStr)
    println(fieldSeq)
    fieldSeq.foreach(a => println(a(2)))
  }

}
