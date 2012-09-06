package com.github.btd.json

import org.specs2.mutable._


class JsonParsingSpec extends Specification {

  def run(str: String) = JsonParser.parse(str)
 
  "json parser " should {
    "parse array with strings and empty arrays" in {
      run("""["asdasd","asdas"]""") must be equalTo (Arr("asdasd", "asdas"))
      run("""["asdasd"]""") must be equalTo (Arr("asdasd"))
      run("""[]""") must be equalTo (Arr())
      run("""["sdfsd", []]""") must be equalTo (Arr("sdfsd", Arr()))
      run("""["sdfsd", ["asdfsad"]]""") must be equalTo (Arr("sdfsd", Arr("asdfsad")))
      run("""[[[]]]""") must be equalTo (Arr(Arr(Arr())))
      run("""[[[], []]]""") must be equalTo (Arr(Arr(Arr(), Arr())))
      run("""["sdfsd", [[],[]], ["asdfsad", []]]""") must be equalTo (Arr("sdfsd", Arr(Arr(), Arr()), Arr("asdfsad", Arr())))
    }

    //TODO specs for strings
    //TODO specs on errors

    "parse array with nulls and arrays" in {
      run("""[null]""") must be equalTo (Arr(Null))
      run("""[null, null]""") must be equalTo (Arr(Null, Null))
      run("""[[null], [null]]""") must be equalTo (Arr(Arr(Null), Arr(Null)))
      run("""[[null, []], null, []]""") must be equalTo (Arr(Arr(Null, Arr()), Null, Arr()))
    }

    "parse array with booleans and arrays" in {
      run("""[true]""") must be equalTo (Arr(True))
      run("""[null, false]""") must be equalTo (Arr(Null, False))
      run("""[[true, false], [null]]""") must be equalTo (Arr(Arr(True, False), Arr(Null)))
      run("""[[null, []], true, []]""") must be equalTo (Arr(Arr(Null, Arr()), True, Arr()))
    }

    "parse objects" in {
      run("""{}""") must be equalTo (Obj())
      run("""{"key": "value"}""") must be equalTo (Obj(("key", Str("value"))))
      run("""{"key": "value" , "key" : true, "key" : false, "key" : null, "key" : [], "key" : {}}""") must be equalTo (Obj(("key", "value"),("key", True), ("key", False), ("key", Null), ("key", Arr()), ("key", Obj())))
      run("""{"key": {"key": [{}, {}]}}""") must be equalTo (Obj(("key", Obj(("key", Arr(Obj(), Obj()))))))
    }
  }
}