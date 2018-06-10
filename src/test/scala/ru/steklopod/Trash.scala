package ru.steklopod

import org.scalatest.{FunSuite, Matchers}
import ru.steklopod.util.GameFieldConverter


class Trash extends FunSuite with Matchers {
  def makeSeqFromStr(s: String): Seq[Int] = {
    s.split(",").map(_.trim.toInt)
  }

  def makeStringFromSeq(seq: Seq[Int]): String = {
    seq.mkString(", ")
  }

  def makeFieldsStringFromSeq(fieldList: Seq[Seq[Int]]): String = {
    fieldList.flatten.mkString(", ")
  }
}
