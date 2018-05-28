package ru.steklopod

import org.scalatest.{FunSuite, Matchers}

class GameApiSpec extends FunSuite with Matchers{

  test("From seq => String and back") {
    val str: String = Helper.makeArrayStringFromSeq(Seq(3, 3, 3))
    print(s" Result String is $str")

    val a: Seq[Int] = Helper.makeSeqFromStr(str)
    print(s"\n Result Seq[Int] is $a")
    a.size should be (3)
  }



}