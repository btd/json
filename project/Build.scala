import sbt._
import Keys._

object JsonBuild extends Build {

  lazy val root = Project(id = "json",
                          base = file(".")) aggregate(core)

  lazy val core = Project(id = "json-core",
                         base = file("core"))

  lazy val benchmark = Project(id = "json-benchmark",
                         base = file("benchmark")) dependsOn(core)

  lazy val serialization = Project(id = "json-serialization",
                         base = file("serialization"))
}