package com.github.btd.json
package ast

//common json value
trait Value

object True extends Value

object False extends Value

object Null extends Value

case class Str(value: String) extends Value

case class NumDouble(num: Double) extends Value

case class NumLong(num: Long) extends Value

case class Arr(elements: List[Value]) extends Value

object Arr {
  def apply():Arr = Arr(List())

  def apply(elements: Value*):Arr = Arr(elements.toList)
}

case class Obj(elements: List[(String, Value)]) extends Value

object Obj {
  def apply():Obj = Obj(List())

  def apply(elements: (String, Value)*):Obj = Obj(elements.toList)
}