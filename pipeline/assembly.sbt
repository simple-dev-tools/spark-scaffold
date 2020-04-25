import sbt._
import sbtassembly.AssemblyKeys.assemblyMergeStrategy
import sbtassembly.{MergeStrategy, PathList}

import scala.sys.process._

lazy val gitCommit = taskKey[String]("git commit version")

gitCommit := {
  ("git rev-parse --short HEAD" !!).stripLineEnd
}

test in assembly := {}

target in assembly := file("package")

assemblyJarName in assembly := {
  name.value + "-" + gitCommit.value + ".jar"
}

assemblyOption in assembly := (assemblyOption in assembly).value.copy(includeScala = false, includeDependency = true)

assemblyMergeStrategy in assembly := {
  {
    case PathList("META-INF", _*) => MergeStrategy.discard
    case _ => MergeStrategy.first
  }
}
