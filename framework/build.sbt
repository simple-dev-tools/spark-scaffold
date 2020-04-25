import Dependencies._

name := "spark-scaffold-framework"

libraryDependencies ++= libDependencies
libraryDependencies ++= testDependencies

publishArtifact in (Compile, packageDoc) := false
mainClass in assembly := None