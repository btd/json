version := "0.1"

organization := "com.github.btd"


scalaBinaryVersion in ThisBuild <<= scalaBinaryVersion { v =>
  if (v.startsWith("2.10"))
    "2.10.0-M7"
  else
    v
}

crossScalaVersions in ThisBuild := Seq("2.9.2", "2.9.1", "2.9.1-1", "2.10.0-M7")


scalacOptions <++= scalaVersion map { v =>
  if (v.startsWith("2.10"))
    Seq("-unchecked", "-deprecation", "-feature", "-language:implicitConversions")
  else
    Seq("-unchecked", "-deprecation")
}

libraryDependencies in ThisBuild <+= 
  scalaVersion { 
    case "2.10.0-M7" => ("org.specs2" % "specs2_2.10.0-M7" % "1.12.1.1" % "test")
    case _ => ("org.specs2" %% "specs2" % "1.12.1" % "test")
  }
