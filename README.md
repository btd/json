Scala Json
========
I was tired with current implementations of json libraries in scala. That is why i wrote another one. I do not worry that anyone will use it. It is written fully by me for myself.
Currently it is json parsing and AST.

To parse:
```scala
import com.github.btd.json._

JsonParser.parse("""{"key": "value" , "key" : true, "key" : false, "key" : null, "key" : [], "key" : {}}""")
//returns Obj(("key", "value"),("key", true), ("key", false), ("key", Null), ("key", Arr()), ("key", Obj()))
```

A bit later i will add printing (scala versions 2.9.* and 2.10.*) and serialization (scala version 2.10.*).

Benchmarks
__________
For benchmarking i used lift-json benchmark, but add my implementation.

My configuration:

* CPU: i5 4 Core
* RAM: 12 Gb

Results (you can try on your machine, and say me results - i am interesting):
```
> run

Multiple main classes detected, select one to run:

 [1] Jsonbench
 [2] main.Main

Enter number: 1

[info] Running Jsonbench 
warmup... done
Jackson 3191ms
warmup... done
lift-json 7073ms
warmup... done
My  4819ms
```

License
_______
Apache Software Foundation 2.0