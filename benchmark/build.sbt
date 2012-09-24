
scalaVersion in ThisBuild := "2.9.2"

resolvers := Seq(
	"Sonatype OSS Releases" at "https://oss.sonatype.org/content/repositories/releases",
	"Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots")

libraryDependencies ++= Seq(
  "net.liftweb" %% "lift-json" % "2.5-SNAPSHOT",
  "org.codehaus.jackson" % "jackson-core-asl" % "1.9.9",
  "org.codehaus.jackson" % "jackson-mapper-asl" % "1.9.9"
)