name := "sclera-tests"

description := "Scripts and data for testing Sclera"

version := "4.0-SNAPSHOT"

homepage := Some(url("https://github.com/scleradb/sclera-tests"))

organization := "com.scleradb"

organizationName := "Sclera, Inc."

organizationHomepage := Some(url("https://www.scleradb.com"))

startYear := Some(2012)

scalaVersion := "2.13.1"

licenses := Seq("Apache License version 2.0" -> url("https://www.apache.org/licenses/LICENSE-2.0.txt"))

libraryDependencies ++= Seq(
    "com.scleradb" %% "sclera-config" % "latest.integration" % "test",
    "com.scleradb" %% "sclera-core" % "latest.integration" % "test",
    "com.scleradb" %% "sclera-jdbc" % "latest.integration" % "test",
    "com.scleradb" %% "sclera-plugin-opennlp" % "latest.integration" % "test",
    "com.scleradb" %% "sclera-plugin-csv" % "latest.integration" % "test",
    "com.scleradb" %% "sclera-plugin-textfiles" % "latest.integration" % "test",
    "com.scleradb" %% "sclera-plugin-matcher" % "latest.integration" % "test",
    "com.scleradb" %% "sclera-sqltests-runner" % "latest.integration" % "test",
    "org.scalatest" %% "scalatest" % "3.1.0" % "test"
)

scalacOptions ++= Seq(
    "-Werror", "-feature", "-deprecation", "-unchecked"
)

fork in Test := true

parallelExecution in Test := false

exportJars := true
