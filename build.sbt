name := "scalajson"

version := "0.1-SNAPSHOT"

//scalaVersion := "2.10.0-M7"
scalaVersion := "2.9.1"

scalacOptions ++= DefaultOptions.scalac

scalacOptions in Compile += Opts.compile.deprecation

scalacOptions in Compile += Opts.compile.unchecked

resolvers ++= Seq (
  "Oss Sonatype" at "https://oss.sonatype.org/content/repositories/releases"
)

libraryDependencies ++= Seq (
  "net.liftweb" %% "lift-json" % "2.5-SNAPSHOT",
  "org.codehaus.jackson" % "jackson-core-asl" % "1.9.9",
  "org.codehaus.jackson" % "jackson-mapper-asl" % "1.9.9",
  "org.specs2" %% "specs2" % "1.12.1" % "test"
    //"org.specs2" % "specs2_2.10.0-M7" % "1.12.1.1" % "test"
)
