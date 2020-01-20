name := "sclera-tests"

description := "Scripts and data for testing Sclera"

version := "4.0-SNAPSHOT"

organization := "com.scleradb"

organizationName := "Sclera, Inc."

organizationHomepage := Some(url("https://www.scleradb.com"))

startYear := Some(2012)

scalaVersion := "2.13.1"

licenses := Seq("Apache License version 2.0" -> url("https://www.apache.org/licenses/LICENSE-2.0.txt"))

libraryDependencies ++= Seq(
    "com.scleradb" %% "sclera-config" % "4.0-SNAPSHOT" % "test",
    "com.scleradb" %% "sclera-core" % "4.0-SNAPSHOT" % "test",
    "com.scleradb" %% "sclera-jdbc" % "4.0-SNAPSHOT" % "test",
    "com.scleradb" %% "sclera-plugin-opennlp" % "4.0-SNAPSHOT" % "test",
    "com.scleradb" %% "sclera-plugin-csv" % "4.0-SNAPSHOT" % "test",
    "com.scleradb" %% "sclera-plugin-textfiles" % "4.0-SNAPSHOT" % "test",
    "com.scleradb" %% "sclera-plugin-matcher" % "4.0-SNAPSHOT" % "test",
    "com.scleradb" %% "sclera-sqltests-parser" % "4.0-SNAPSHOT" % "test",
    "org.scalatest" %% "scalatest" % "3.1.0" % "test"
)

scalacOptions ++= Seq(
    "-Werror", "-feature", "-deprecation", "-unchecked"
)

javaOptions += "-DSCLERA_HOME=%s".format(Path.userHome / ".sclera")

fork in Test := true

parallelExecution in Test := false

exportJars := true
