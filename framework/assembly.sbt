import sbt._
import scala.sys.process._
import sbtassembly.AssemblyKeys.assemblyMergeStrategy
import sbtassembly.{MergeStrategy, PathList}

lazy val gitCommit = taskKey[String]("git commit version")

gitCommit := { ("git rev-parse --short HEAD" !!).stripLineEnd }

test in assembly := {}

assemblyOption in assembly := (assemblyOption in assembly).value.copy(includeScala = false, includeDependency = true)

assemblyMergeStrategy in assembly := {
  {
    case PathList("META-INF", _*) => MergeStrategy.discard
    case _ => MergeStrategy.first
  }
}

assemblyJarName in assembly := {
  name.value + "-" + version.value + "-" + gitCommit.value + ".jar"
}

target in assembly := file("package/lib")
