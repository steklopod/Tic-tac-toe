package ru.steklopod.util

import ru.steklopod.util.MyJsonProtocol._
import spray.json._


object GameFieldConverter {
  def convertFieldFromStringToVector(str: String): Vector[Vector[Int]] = str.parseJson.convertTo[Vector[Vector[Int]]]

  def convertFieldFromVectorToString(fieldVector: Vector[Vector[Int]]): String = fieldVector.toJson.toString

  def convertSizeFromStrToVector(size: String): Vector[Int] = size.parseJson.convertTo[Vector[Int]]

  def makeFieldFromSize(fieldSize: Vector[Int]): Vector[Vector[Int]] = {
    val width = fieldSize(0)
    val height = fieldSize(1)
    Vector.fill(height, width)(0)
  }

  @throws(classOf[IllegalArgumentException])
  def convertFieldFromVectorToStr(sizeOfPlayField: Vector[Int]): String = {
    require(sizeOfPlayField.size == 2, "Field size must have 2 elements: HEIGHT & WIDTH only.")
    sizeOfPlayField.toArray[Int].toJson.toString()
  }

  @deprecated
  final case object ThreeByThree extends GameFieldConverter {
    override def toString: String = "[3, 3]"
  }

}

sealed trait GameFieldConverter