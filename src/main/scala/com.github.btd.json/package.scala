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

  val JsonParser = reader.JsonParser

  implicit def stringToJsonStr(str: String) = Str(str)
}