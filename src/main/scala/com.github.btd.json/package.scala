package com.github.btd

package object json {
  type Value = ast.Value

  val True = ast.True
  val False = ast.False

  val Null = ast.Null

  type Str = ast.Str
  val Str = ast.Str

  type Arr = ast.Arr
  val Arr = ast.Arr

  type Obj = ast.Obj
  val Obj = ast.Obj

  type NumDouble = ast.NumDouble
  val NumDouble = ast.NumDouble

  type NumLong = ast.NumLong
  val NumLong = ast.NumLong

  val JsonParser = reader.JsonParser

  val JsonPrinter = printer.JsonPrinter

  implicit def stringToJsonStr(str: String) = Str(str)

  implicit def booleanToJsonBool(bool: Boolean) = if(bool) True else False

  implicit def doubleToNumDouble(double: Double) = NumDouble(double)

  implicit def longToNumLong(long: Long) = NumLong(long)
}