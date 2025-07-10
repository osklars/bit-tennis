
ThisBuild / version := "0.1.0-SNAPSHOT"

val Http4sVersion = "0.23.30"

ThisBuild / scalaVersion := "3.3.6"
Compile / run / fork := true

lazy val root = (project in file("."))
  .settings(
    name := "vision",
    libraryDependencies ++= Seq(
      "org.http4s" %% "http4s-ember-server" % Http4sVersion,
      "org.http4s" %% "http4s-ember-client" % Http4sVersion,
      "org.http4s" %% "http4s-dsl" % Http4sVersion,
      "com.lihaoyi" %% "upickle" % "4.2.1",
      "ai.dragonfly" %% "slash" % "0.3.21",
    ),
  )
