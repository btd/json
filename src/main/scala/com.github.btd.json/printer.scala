package com.github.btd.json
package printer

import ast._

trait Printer {
  def print(value: Value): String

  def quote(s: String): String = {
    val buf = new StringBuilder
    for (i <- 0 until s.length) {
      val c = s.charAt(i)
      buf.append(c match {
        case '"'  => "\\\""
        case '\\' => "\\\\"
        case '\b' => "\\b"
        case '\f' => "\\f"
        case '\n' => "\\n"
        case '\r' => "\\r"
        case '\t' => "\\t"
        case c if ((c >= '\u0000' && c < '\u001f') || (c >= '\u0080' && c < '\u00a0') || (c >= '\u2000' && c < '\u2100')) => "\\u%04x".format(c: Int)
        case c => c
      })
    }
    buf.toString
  }
}

class CompactPrinter extends Printer {
  def print(value: Value):String = {
    value match {
      case True => "true"
      case False => "false"
      case Null => "null"
      case Str(str) => "\"" + quote(str) + "\""
      case NumDouble(num) => num.toString
      case NumLong(num) => num.toString
      case Arr(elements) => elements.map(print).mkString("[", ",", "]")
      case Obj(pairs) => pairs.map(p => "\"" + quote(p._1) + "\"" + ":" + print(p._2)).mkString("{", ",", "}")
    }
  }
}

object JsonPrinter {
  private val compactPrinter = new CompactPrinter
  def compact(value: Value) = compactPrinter.print(value)
}