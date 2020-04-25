import scala.io.Source

name := "spark-scaffold"

ThisBuild / cancelable := true

ThisBuild / scalaVersion := "2.11.12"
ThisBuild / organization := "com.log2"
ThisBuild / javacOptions ++= Seq(
  "-Xlint:deprecation",
  "-Xlint:unchecked",
  "-source", "1.8",
  "-target", "1.8",
  "-g:vars"
)

ThisBuild / logLevel := Level.Warn

// Only show warnings and errors on the screen for compilations.
// This applies to both test:compile and compile and is Info by default
ThisBuild / logLevel in compile := Level.Warn

// Level.INFO is needed to see detailed output when running tests
ThisBuild / logLevel in Test := Level.Info

ThisBuild / exportJars := true

ThisBuild / scalacOptions ++= Seq(
  "-deprecation",
  "-encoding", "UTF-8",
  "-feature",
  "-target:jvm-1.8",
  "-unchecked",
  "-Xfatal-warnings",
  "-Ywarn-adapted-args",
  "-Ywarn-dead-code",
  "-Ywarn-unused-import",
  "-Ypartial-unification",
  "-Xfuture",
  "-Xlint"
)

// The REPL can’t cope with -Ywarn-unused:imports or -Xfatal-warnings so turn them off for the console
ThisBuild / scalacOptions in (Compile, console) --= Seq("-Ywarn-unused-import", "-Xfatal-warnings")

lazy val root = (project in file("."))
  .aggregate(framework, pipeline)
  .dependsOn(framework, pipeline)

lazy val framework = (project in file("framework"))

lazy val pipeline = (project in file("pipeline"))
  .aggregate(framework)
  .dependsOn(framework % "test->test;compile->compile")

ThisBuild / resolvers ++= Seq(
  "Spark Packages Repo" at "http://dl.bintray.com/spark-packages/maven"
)


ThisBuild / fork := true
ThisBuild / outputStrategy := Some(StdoutOutput)
ThisBuild / javaOptions ++= Seq("-Xss8M", "-XX:+CMSClassUnloadingEnabled")

ThisBuild / version := Source.fromFile(".project-version").getLines.mkString

// Individual test runtime
ThisBuild / testOptions in Test += Tests.Argument(TestFrameworks.ScalaTest, "-oD")
