Scala Json
========
I was tired with current implementations of json libraries in scala. That is why i wrote another one (currently it is only strict json). I do not worry that anyone will use it. It is written fully by me for myself.
Currently it is json parsing, printing and AST.

Main ideas i taked from usage of google gson and lift json.

To use this library you only need to import package `com.github.btd.json`

To parse:
```scala
import com.github.btd.json._

JsonParser.parse("""{"key": "value" , "key" : true, "key" : false, "key" : null, "key" : [], "key" : {}}""")
//returns Obj(("key", "value"),("key", true), ("key", false), ("key", Null), ("key", Arr()), ("key", Obj()))
```

To print:
```scala
import com.github.btd.json._//it is only one import you never need to work with this library

JsonPrinter.compact(Arr())
//returns "[]"
```

A bit later i will add serialization (scala version 2.10.*).


Benchmarks
----------
For benchmarking i used lift-json benchmark, but add my implementation.

My configuration:

* CPU: i5 4 Core
* RAM: 12 Gb

Java:
```
mac:json den$ java -version
java version "1.6.0_35"
Java(TM) SE Runtime Environment (build 1.6.0_35-b10-428-11M3811)
Java HotSpot(TM) 64-Bit Server VM (build 20.10-b01-428, mixed mode)
```

Results (you can try on your machine, and say me results - i am interesting):
```
> run
[info] Running Jsonbench 
warmup... done
Jackson 3191ms
warmup... done
lift-json 7073ms
warmup... done
My  4819ms
```

License
-------
Apache Software Foundation 2.0