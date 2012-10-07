package com.github.btd.json
package ast

//common json value
trait JValue

object JTrue extends JValue

object JFalse extends JValue

object JNull extends JValue

case class JStr(value: String) extends JValue

trait JNum[A] extends JValue {
	def value: A
}

case class JDouble(value: Double) extends JNum[Double]

case class JLong(value: Long) extends JNum[Long]

case class JArr(elements: List[JValue]) extends JValue

object JArr {
  def apply():JArr = JArr(Nil)

  def apply(elements: JValue*):JArr = JArr(elements.toList)
}

case class JObj(elements: List[(String, JValue)]) extends JValue

object JObj {
  def apply():JObj = JObj(Nil)

  def apply(elements: (String, JValue)*):JObj = JObj(elements.toList)
}