version := "0.1"

organization := "com.github.btd"


scalaBinaryVersion in ThisBuild <<= scalaBinaryVersion { v =>
  if (v.startsWith("2.10"))
    "2.10"
  else
    v
}

resolvers += Resolver.sonatypeRepo("snapshots")

crossScalaVersions := Seq("2.9.2", "2.9.1", "2.9.1-1", "2.10.0-SNAPSHOT")


scalacOptions <++= scalaVersion map { v =>
  if (v.startsWith("2.10"))
    Seq("-unchecked", "-deprecation", "-feature", "-language:implicitConversions")
  else
    Seq("-unchecked", "-deprecation")
}

libraryDependencies <+= 
  scalaVersion { 
    case v if (v.startsWith("2.10")) => ("org.specs2" % "specs2_2.10.0-M7" % "1.12.1.1" % "test")
    case _ => ("org.specs2" %% "specs2" % "1.12.1" % "test")
  }
