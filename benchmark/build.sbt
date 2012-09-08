scalaVersion := "2.9.1"

resolvers := Seq("Oss Sonatype" at "https://oss.sonatype.org/content/repositories/releases")

libraryDependencies ++= Seq(
  "net.liftweb" %% "lift-json" % "2.5-SNAPSHOT",
  "org.codehaus.jackson" % "jackson-core-asl" % "1.9.9",
  "org.codehaus.jackson" % "jackson-mapper-asl" % "1.9.9"
)