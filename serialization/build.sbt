version := "0.1"

organization := "com.github.btd"

scalaVersion := "2.10.0-M7"

scalaBinaryVersion <<= scalaBinaryVersion { v =>
  if (v.startsWith("2.10"))
    "2.10.0-M7"
  else
    v
}

scalacOptions <++= scalaVersion map { v =>
  if (v.startsWith("2.10"))
    Seq("-unchecked", "-deprecation", "-feature", "-language:implicitConversions")
  else
    Seq("-unchecked", "-deprecation")
}

libraryDependencies <<= scalaVersion { scala_version =>
    Seq(
        "org.scala-lang" % "scala-reflect" % scala_version % "provided",
        "org.specs2" % "specs2_2.10.0-M7" % "1.12.1.1" % "test"
    )
}