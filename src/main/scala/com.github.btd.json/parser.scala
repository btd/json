package com.github.btd.json
package reader

import ast._

import java.io.{Reader, StringReader}

object Token extends Enumeration {
  val ObjectBegin, ObjectEnd, ArrayBegin, ArrayEnd, DoubleQuoteStrBegin, Null, True, False, Eof = Value
}

object Scope extends Enumeration {
  val Nothing, ArrEmpty, ArrNext, ArrNonEmpty, ObjEmpty, ObjKey, ObjVal, ObjNext, ObjNonEmpty = Value
}

class JsonReader(in: Reader) {
  def this(str: String) = this(new StringReader(str))
  require(in ne null)
  require(in.ready)

  private var defaultStackSize = 32

  private var stack = new Array[Scope.Value](defaultStackSize)
  private var stackPos = 0

  private val charBufferLength = 1024

  private val buffer = new Array[Char](charBufferLength)
  private var bufferPosition = 0
  private var bufferFillSize = 0

  private var peeked: Token.Value = _ //invalid before peek() invoked, do not use it for prediction, only for consistency

  fillBufferAndReset()
  push(Scope.Nothing)

  @inline
  private def top() = stack(stackPos - 1)

  @inline
  private def pop() = stackPos -= 1

  @inline
  private def was(token: Token.Value) = peeked == token

  def beginArray() {
    require(was(Token.ArrayBegin))
    push(Scope.ArrEmpty)
  }

  def endArray() {
    require(was(Token.ArrayEnd))
    pop()
  }

  def beginObject() {
    require(was(Token.ObjectBegin))
    push(Scope.ObjEmpty)
  }

  def endObject() {
    require(was(Token.ObjectEnd))
    pop()
  }

  def nextString(): String = {
    peeked match {
      case Token.DoubleQuoteStrBegin => nextQuotedString('\"')
      case _ => throw new IllegalStateException("Previously was readed not a string")
    }
  }

  private def nextValue() {
    top() match { 
      case s if s == Scope.ArrEmpty || s == Scope.ArrNonEmpty => 
       pop()
       push(Scope.ArrNext)

      case Scope.ObjVal =>
       pop()
       push(Scope.ObjNext)

      case s => throw new IllegalStateException("Not possible state: " + s)
    }
  }

  def peek(): Token.Value = {
    //println("Peeking " + top() + " was " + peeked)
    top() match {
      case Scope.Nothing => //begining
        nextNonWhitespace() match {
          case '{' => saveAndGet(Token.ObjectBegin)
            
          case '[' => saveAndGet(Token.ArrayBegin)
            
          case c => throw new SyntaxException("Found " + c)
        }

      case Scope.ArrEmpty => 
        nextNonWhitespace() match {
          case ']' => saveAndGet(Token.ArrayEnd)

          case '"' =>
            nextValue()
            saveAndGet(Token.DoubleQuoteStrBegin)

          case '[' => 
            nextValue()
            saveAndGet(Token.ArrayBegin)

          case '{' => 
            nextValue()
            saveAndGet(Token.ObjectBegin)

          case 'n' =>
            nextValue()
            checkKeyword("null")
            saveAndGet(Token.Null)
          
          case 't' =>
            nextValue()
            checkKeyword("true")
            saveAndGet(Token.True)

          case 'f' =>
            nextValue()
            checkKeyword("false")
            saveAndGet(Token.False)

          //number

          case c => throw new SyntaxException("Found " + c + " buffer: " + new String(buffer) + " pos =" + bufferPosition)
        }
      case Scope.ArrNonEmpty => 
        nextNonWhitespace() match {

          case '"' =>
            nextValue()
            saveAndGet(Token.DoubleQuoteStrBegin)

          case '[' => 
            nextValue()
            saveAndGet(Token.ArrayBegin)

          case '{' => 
            nextValue()
            saveAndGet(Token.ObjectBegin)

          case 'n' =>
            nextValue()
            checkKeyword("null")
            saveAndGet(Token.Null)
          
          case 't' =>
            nextValue()
            checkKeyword("true")
            saveAndGet(Token.True)

          case 'f' =>
            nextValue()
            checkKeyword("false")
            saveAndGet(Token.False)

          //number

          case c => throw new SyntaxException("Found " + c + " buffer: " + new String(buffer) + " pos =" + bufferPosition)
        }
      case Scope.ArrNext => //state when we finish parse element of array, next should be array end or comma
        nextNonWhitespace() match {
          case ']' => saveAndGet(Token.ArrayEnd)

          case ',' => //prepare to next array element
            pop()
            push(Scope.ArrNonEmpty)
            peek()

          case c => throw new SyntaxException("Found " + c)
        }

      case Scope.ObjEmpty => // next can be end of object or key
        nextNonWhitespace() match {
          case '"' =>
            pop()
            push(Scope.ObjKey)
            saveAndGet(Token.DoubleQuoteStrBegin)

          case '}' =>
            saveAndGet(Token.ObjectEnd)

          case c => throw new SyntaxException("Found " + c + " buffer: " + new String(buffer) + " pos =" + bufferPosition)
        }

      case Scope.ObjNonEmpty => // next can be end of object or key
        nextNonWhitespace() match {
          case '"' =>
            pop()
            push(Scope.ObjKey)
            saveAndGet(Token.DoubleQuoteStrBegin)

          case c => throw new SyntaxException("Found " + c + " buffer: " + new String(buffer) + " pos =" + bufferPosition)
        }

      case Scope.ObjKey => // readed object key, now should be :
        nextNonWhitespace() match {
          case ':' =>
            pop()
            push(Scope.ObjVal)
            peek()

          case c => throw new SyntaxException("Found " + c + " buffer: " + new String(buffer) + " pos =" + bufferPosition)
        }

      case Scope.ObjVal => // readed object key, now should be :
        nextNonWhitespace() match {

          case '"' =>
            nextValue()
            saveAndGet(Token.DoubleQuoteStrBegin)

          case '[' => 
            nextValue()
            saveAndGet(Token.ArrayBegin)

          case '{' => 
            nextValue()
            saveAndGet(Token.ObjectBegin)

          case 'n' =>
            nextValue()
            checkKeyword("null")
            saveAndGet(Token.Null)
          
          case 't' =>
            nextValue()
            checkKeyword("true")
            saveAndGet(Token.True)

          case 'f' =>
            nextValue()
            checkKeyword("false")
            saveAndGet(Token.False)

          //number

          case c => throw new SyntaxException("Found " + c + " buffer: " + new String(buffer) + " pos =" + bufferPosition)
        }

      case Scope.ObjNext => //we just read { "key": Value  next should be , or }
        nextNonWhitespace() match {
          case '}' => saveAndGet(Token.ObjectEnd)

          case ',' => //prepare to next object key-value element
            pop()
            push(Scope.ObjNonEmpty)
            peek()

          case c => throw new SyntaxException("Found " + c)
        }

      case _ => throw new IllegalStateException("Not implemented")
    }
  }

  /**
    we do not check first letter, assume we already read it and know that it is valid
  */
  private def checkKeyword(keyword: String) {
    (1 until keyword.length) foreach { i =>
      if(next() != keyword.charAt(i)) {
        throw new SyntaxException("Incorrect keyword: " + keyword)
      }
    }
  }

  private def saveAndGet(token: Token.Value) = {
    peeked = token
    token
  }

  private def push(token: Scope.Value) = {
    if(stackPos == stack.length) {
      val newStack = new Array[Scope.Value](stack.length * 2)
      System.arraycopy(stack, 0, newStack, 0, stack.length)
      stack = newStack
    }
    stack(stackPos) = token
    stackPos += 1
  }

  private def next(): Char = {
    if(bufferPosition == bufferFillSize) fillBufferAndReset()

    bufferPosition += 1
    buffer(bufferPosition - 1)
  }

  private def nextNonWhitespace(): Char = {
    var nextChar = next()
    while(Character.isWhitespace(nextChar)) {
      nextChar = next()
    }
    nextChar
  }

  private def nextQuotedString(quote: Char): String = {
    //println("Readeing quoted string " + quote)
    val buf = new StringBuilder
    var beginPos = bufferPosition
    var length = 0
    var nextChar = next()
    while(nextChar != quote) {
      //println("Next char " + nextChar)
      if(nextChar == '\\') {
        buf.appendAll(buffer, beginPos, length)
        buf.append(nextEscapeChar())
        beginPos = bufferPosition
        length = 0
      }
      //println("filled?:  " + bufferFilled)
      if(bufferFilled) {
        buf.appendAll(buffer, beginPos, length)
        fillBufferAndReset()
        beginPos = bufferPosition
        length = 0
      } else {
        length += 1
        nextChar = next()
      }
    }
    buf.appendAll(buffer, beginPos, length)
    //println("buffer: " + buf.toString)
    buf.toString
  }

  private def nextEscapeChar(): Char = {
    next() match {
      case 'u' =>
        var result: Int = 0
        result += 1
        (1 to 4) foreach { i =>
          result <<= 4
          next().toInt match {
            case c if c >= 48 && c <= 57 => 
              result += c - 48 //'0' == 48
            case c if c >= 97 && c <= 102 => 
              result += c - 97 + 10// 'a' = 97
            case c if c >= 65 && c <= 70 => 
              result += c - 65 + 10// 'A' = 65
            case _ => throw new NumberFormatException("Illegal unicode escape sequence")
          }
        }
        result.toChar
      case 't' => '\t'
      case 'b' => '\b'
      case 'n' => '\n'
      case 'r' => '\r'
      case 'f' => '\f'
      case '\n' => '\n'
      case '\'' => '\''
      case '\"' => '\"'
      case '\\' => '\\'
      case c => c
    }
  }

  @inline
  private def bufferFilled = bufferPosition == bufferFillSize

  /**
    fill buffer with new portion of chars and reset position to 0
  */
  private def fillBufferAndReset() {
    bufferPosition = 0
    bufferFillSize = in.read(buffer)
  }

  /**
    return true if end reached
  */
  @inline
  private def endReached() = {
    bufferFillSize == -1
  }
}

class JsonParser(r: JsonReader) {
  private def parseArray(): Arr = {
    val arr = new collection.mutable.ListBuffer[Value]
    r.beginArray()

    var t = r.peek()
    while(t != Token.ArrayEnd) {
      //println("Filling array " + t)
      arr += processToken(t)
      t = r.peek()
    }

    r.endArray()
    Arr(arr.toList)
  }

  private def parseObject(): Obj = {
    val arr = new collection.mutable.ListBuffer[(String, Value)] //TODO maybe better to replace there to Map[String, Value] ?

    r.beginObject()
    var t = r.peek()
    while(t != Token.ObjectEnd) {
      arr += ((r.nextString(), processToken(r.peek())))
      t = r.peek()
    }
    r.endObject()

    Obj(arr.toList)
  }

  private def processToken(token: Token.Value) = {
    //println("Processing token " + token)
    token match {
      case Token.ArrayBegin => parseArray()
      case Token.ObjectBegin => parseObject()
      case Token.DoubleQuoteStrBegin => Str(r.nextString())
      case Token.Null => Null
      case Token.True => True
      case Token.False => False
    }
  }
  def parse(): Value = processToken(r.peek())
}

object JsonParser {
  def parse(str: String) = new JsonParser(new JsonReader(str)).parse()
  def parse(reader: Reader) = new JsonParser(new JsonReader(reader)).parse()
}

class ParserException(str: String) extends Exception(str)

class SyntaxException(str: String) extends Exception(str)
