ThisBuild / version := "0.1.0-SNAPSHOT"

val Http4sVersion = "0.23.30"

ThisBuild / scalaVersion := "3.3.4"
Compile / run / fork := true


lazy val root = (project in file("."))
  .settings(
    name := "vision",
    libraryDependencies ++= Seq(
      "org.http4s" %% "http4s-ember-server" % Http4sVersion,
      "org.http4s" %% "http4s-ember-client" % Http4sVersion,
      "org.http4s" %% "http4s-dsl" % Http4sVersion,
      "com.lihaoyi" %% "upickle" % "4.1.0",
      "ai.dragonfly" %% "slash" % "0.3.2",
      "org.slf4j" % "slf4j-api" % "2.0.16",
      "ch.qos.logback" % "logback-classic" % "1.5.16"
    )
  )
