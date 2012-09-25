version := "0.1"

organization := "com.github.btd"

scalaVersion := "2.10.0-SNAPSHOT"

scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature", "-language:implicitConversions")

resolvers += Resolver.sonatypeRepo("snapshots")

libraryDependencies <<= scalaVersion { scala_version =>
    Seq(
        "org.scala-lang" % "scala-reflect" % scala_version % "provided",
        "com.github.btd" %% "json-core" % "0.1"
    )
}