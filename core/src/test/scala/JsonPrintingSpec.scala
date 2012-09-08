package com.github.btd.json

import org.specs2.mutable._


class JsonPrintingSpec extends Specification {

  def run(value: Value) = JsonPrinter.compact(value)
 
  "json printer " should {
    "compactly print objects and arrays" in {
      run(Arr("asdasd", "asdas")) must be equalTo ("""["asdasd","asdas"]""")
      run(Arr("a1b\n", "asdas")) must be equalTo ("""["a\u0031b\n","asdas"]""")
      run(Arr("asdasd")) must be equalTo ("""["asdasd"]""")
      run(Arr()) must be equalTo ("""[]""")
      run(Arr("sdfsd", Arr())) must be equalTo ("""["sdfsd",[]]""")
      run(Arr("sdfsd", Arr("asdfsad"))) must be equalTo ("""["sdfsd",["asdfsad"]]""")
      run(Arr(Arr(Arr()))) must be equalTo ("""[[[]]]""")
      run(Arr(Arr(Arr(), Arr()))) must be equalTo ("""[[[],[]]]""")
      run(Arr("sdfsd", Arr(Arr(), Arr()), Arr("asdfsad", Arr()))) must be equalTo ("""["sdfsd",[[],[]],["asdfsad",[]]]""")
      run(Obj(("key", "value"),("key", true), ("key", false), ("key", Null), ("key", Arr()), ("key", Obj()))) must be equalTo ("""{"key":"value","key":true,"key":false,"key":null,"key":[],"key":{}}""")
    }
  }
}
