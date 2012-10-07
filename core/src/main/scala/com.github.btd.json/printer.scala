package com.github.btd.json
package printer

import ast._

trait Printer {
  def print(value: JValue): String

  protected def quote(s: String): String = {
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
  def print(value: JValue):String = {
    value match {
      case JTrue => "true"
      case JFalse => "false"
      case JNull => "null"
      case JStr(str) => "\"" + quote(str) + "\""
      case JDouble(num) => num.toString
      case JLong(num) => num.toString
      case JArr(elements) => elements.map(print).mkString("[", ",", "]")
      case JObj(pairs) => pairs.map(p => "\"" + quote(p._1) + "\"" + ":" + print(p._2)).mkString("{", ",", "}")
    }
  }
}

/*
  This one almost all taken from lift-json
  Need only for debuging purpose
*/
class PrettyPrinter extends Printer {
  import text.Document
  import text.Document._

  def print(value: JValue): String = {
    val writer = new java.io.StringWriter
    documentWrap(value).format(0, writer)
    writer.toString
  }

  private def punctuate(p: Document, docs: List[Document]): Document = 
    if (docs.isEmpty) empty
    else docs.reduceLeft((d1, d2) => d1 :: p :: d2)

  private def documentWrap(value: JValue): Document = {
    value match {
      case JTrue => text("true")
      case JFalse => text("false")
      case JNull => text("null")
      case JStr(str) => text("\"" + quote(str) + "\"")
      case JDouble(num) => text(num.toString)
      case JLong(num) => text(num.toString)
      case JArr(elements) => text("[") :: punctuate(text(","), elements.map(documentWrap)) :: text("]")
      case JObj(pairs) => 
        text("{") :: 
        nest(2, break :: 
          punctuate(text(",") :: 
          break, pairs.map(f => text("\"" + quote(f._1) + "\": ") :: 
          documentWrap(f._2)))) :: 
        break :: 
        text("}")
    }
  }
}

object JsonPrinter {
  private val compactPrinter = new CompactPrinter
  private val prettyPrinter = new PrettyPrinter
  def compact(value: JValue) = compactPrinter.print(value)
  def pretty(value: JValue) = prettyPrinter.print(value)
}