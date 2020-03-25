name := """sbt run"""
organization := "com.example"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.13.1"

libraryDependencies +=guice

libraryDependencies ++=Seq(
  "com.typesafe.play" %% "play" % "2.7.3",
  "org.elasticsearch" % "elasticsearch" % "7.6.1",
  "org.elasticsearch.client" % "elasticsearch-rest-client" % "7.6.1",
  "org.elasticsearch.client" % "elasticsearch-rest-high-level-client" % "7.6.1",
  "org.apache.httpcomponents" % "httpasyncclient" % "4.1.4"
)

