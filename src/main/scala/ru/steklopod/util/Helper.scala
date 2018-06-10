package ru.steklopod.util

import scala.collection.mutable.ListBuffer
import spray.json._
import ru.steklopod.util.MyJsonProtocol._

object Helper {
  def strToVector(steps: String): Vector[Int] = steps.parseJson.convertTo[Vector[Int]]

  @throws(classOf[IllegalArgumentException])
  def vectorToStr(sizeOfPlayField: Vector[Int]): String = {
    require(sizeOfPlayField.size == 2, "Field size must have 2 elements: height & width only.")
    sizeOfPlayField.toArray[Int].toJson.toString()
  }

  final case object ThreeByThree extends Helper {
    override def toString: String = "3, 3"
  }

  final case object ForByFour extends Helper {
    override def toString: String = "4, 4"
  }

  def makeSeqFromStr(s: String): Seq[Int] = {
    s.split(",").map(_.trim.toInt)
  }

  def makeStringFromSeq(seq: Seq[Int]): String = {
    seq.mkString(", ")
  }

  def getFieldListFromString(gameFieldStr: String): Seq[Seq[Int]] = {
    val gameFieldSeq: Seq[Int] = makeSeqFromStr(gameFieldStr)

    val fieldHigh = makeSeqFromStr(Helper.ThreeByThree.toString)(1) //TODO - изменение размера поля
    require(gameFieldSeq.size == 9, "Fiield size must be 3:3. Sorry.")

    val fieldsSeq = new ListBuffer[Seq[Int]]()
    var from = 0
    for (n <- 1 to fieldHigh) {
      var to = fieldHigh * n
      val tuple = gameFieldSeq.slice(from, to)
      from = to
      fieldsSeq += tuple
    }
    fieldsSeq
  }

  def makeFieldsStringFromSeq(fieldList: Seq[Seq[Int]]): String = {
    fieldList.flatten.mkString(", ")
  }

  def fromString(s: String): Helper = s match {
    case "3, 3" => Helper.ThreeByThree
    //  case "4, 4" => Helper.ForByFour
    case c => throw new IllegalArgumentException(s"There is no such size as $c yet. Sorry :-(")
  }

}

sealed trait Helper