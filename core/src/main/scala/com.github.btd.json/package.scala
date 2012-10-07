package com.github.btd

package object json {
  type JValue = ast.JValue

  val JTrue = ast.JTrue
  val JFalse = ast.JFalse

  val JNull = ast.JNull

  type JStr = ast.JStr
  val JStr = ast.JStr

  type JArr = ast.JArr
  val JArr = ast.JArr

  type JObj = ast.JObj
  val JObj = ast.JObj

  type JDouble = ast.JDouble
  val JDouble = ast.JDouble

  type JLong = ast.JLong
  val JLong = ast.JLong

  val JsonParser = reader.JsonParser

  val JsonPrinter = printer.JsonPrinter

  implicit def stringToJsonStr(str: String) = JStr(str)

  implicit def booleanToJsonBool(bool: Boolean) = if(bool) JTrue else JFalse

  implicit def doubleToNumDouble(double: Double) = JDouble(double)

  implicit def longToNumLong(long: Long) = JLong(long)
}