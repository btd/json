package com.github.btd.json

import org.specs2.mutable._


class JsonPrintingSpec extends Specification {

  def run(value: JValue) = JsonPrinter.compact(value)
 
  "json printer " should {
    "compactly print objects and arrays" in {
      run(JArr("asdasd", "asdas")) must be equalTo ("""["asdasd","asdas"]""")
      run(JArr("a1b\n", "asdas")) must be equalTo ("""["a\u0031b\n","asdas"]""")
      run(JArr("asdasd")) must be equalTo ("""["asdasd"]""")
      run(JArr()) must be equalTo ("""[]""")
      run(JArr("sdfsd", JArr())) must be equalTo ("""["sdfsd",[]]""")
      run(JArr("sdfsd", JArr("asdfsad"))) must be equalTo ("""["sdfsd",["asdfsad"]]""")
      run(JArr(JArr(JArr()))) must be equalTo ("""[[[]]]""")
      run(JArr(JArr(JArr(), JArr()))) must be equalTo ("""[[[],[]]]""")
      run(JArr("sdfsd", JArr(JArr(), JArr()), JArr("asdfsad", JArr()))) must be equalTo ("""["sdfsd",[[],[]],["asdfsad",[]]]""")
      run(JObj(("key", "value"),("key", true), ("key", false), ("key", JNull), ("key", JArr()), ("key", JObj()))) must be equalTo ("""{"key":"value","key":true,"key":false,"key":null,"key":[],"key":{}}""")
    }
  }
}
