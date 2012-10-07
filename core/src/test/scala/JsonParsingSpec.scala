package com.github.btd.json

import org.specs2.mutable._


class JsonParsingSpec extends Specification {

  def run(str: String) = JsonParser.parse(str)
 
  "json parser " should {
    "parse array with strings and empty arrays" in {
      run("""["asdasd","asdas"]""") must be equalTo (JArr("asdasd", "asdas"))
      run("""["a\u0031b\n","asdas"]""") must be equalTo (JArr("a1b\n", "asdas"))
      run("""["asdasd"]""") must be equalTo (JArr("asdasd"))
      run("""[]""") must be equalTo (JArr())
      run("""["sdfsd", []]""") must be equalTo (JArr("sdfsd", JArr()))
      run("""["sdfsd", ["asdfsad"]]""") must be equalTo (JArr("sdfsd", JArr("asdfsad")))
      run("""[[[]]]""") must be equalTo (JArr(JArr(JArr())))
      run("""[[[], []]]""") must be equalTo (JArr(JArr(JArr(), JArr())))
      run("""["sdfsd", [[],[]], ["asdfsad", []]]""") must be equalTo (JArr("sdfsd", JArr(JArr(), JArr()), JArr("asdfsad", JArr())))
    }

    "parse array with numbers" in {
      run("""[-0, 0, 0.1, "123", 100, null]""") must be equalTo (JArr(0, 0, 0.1, "123", 100, JNull))
    }

    " throws exception if document is not a strict json" in {
      run("[") must throwA[reader.SyntaxException]
      run("{") must throwA[reader.SyntaxException]
      run("1231") must throwA[reader.SyntaxException]
      run("[nul]") must throwA[reader.SyntaxException]
      run("{null: null}") must throwA[reader.SyntaxException]
    }

    "parse array with nulls and arrays" in {
      run("""[null]""") must be equalTo (JArr(JNull))
      run("""[null, null]""") must be equalTo (JArr(JNull, JNull))
      run("""[[null], [null]]""") must be equalTo (JArr(JArr(JNull), JArr(JNull)))
      run("""[[null, []], null, []]""") must be equalTo (JArr(JArr(JNull, JArr()), JNull, JArr()))
    }

    "parse array with booleans and arrays" in {
      run("""[true]""") must be equalTo (JArr(true))
      run("""[null, false]""") must be equalTo (JArr(JNull, false))
      run("""[[true, false], [null]]""") must be equalTo (JArr(JArr(true, false), JArr(JNull)))
      run("""[[null, []], true, []]""") must be equalTo (JArr(JArr(JNull, JArr()), true, JArr()))
    }

    "parse objects" in {
      run("""{}""") must be equalTo (JObj())
      run("""{"key": "value"}""") must be equalTo (JObj(("key", JStr("value"))))
      run("""{"key": "value" , "key" : true, "key" : false, "key" : null, "key" : [], "key" : {}}""") must be equalTo (JObj(("key", "value"),("key", true), ("key", false), ("key", JNull), ("key", JArr()), ("key", JObj())))
      run("""{"key": {"key": [{}, {}]}}""") must be equalTo (JObj(("key", JObj(("key", JArr(JObj(), JObj()))))))
    }
  }
}
