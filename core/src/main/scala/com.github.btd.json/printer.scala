package com.github.btd.json
package printer

import ast._

trait Printer {
  def print(value: Value): String

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

/*
  This one almost all taken from lift-json
  Need only for debuging purpose
*/
class PrettyPrinter extends Printer {
  import text.Document
  import text.Document._

  def print(value: Value): String = {
    val writer = new java.io.StringWriter
    documentWrap(value).format(0, writer)
    writer.toString
  }

  private def punctuate(p: Document, docs: List[Document]): Document = 
    if (docs.isEmpty) empty
    else docs.reduceLeft((d1, d2) => d1 :: p :: d2)

  private def documentWrap(value: Value): Document = {
    value match {
      case True => text("true")
      case False => text("false")
      case Null => text("null")
      case Str(str) => text("\"" + quote(str) + "\"")
      case NumDouble(num) => text(num.toString)
      case NumLong(num) => text(num.toString)
      case Arr(elements) => text("[") :: punctuate(text(","), elements.map(documentWrap)) :: text("]")
      case Obj(pairs) => 
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
  def compact(value: Value) = compactPrinter.print(value)
  def pretty(value: Value) = prettyPrinter.print(value)
}