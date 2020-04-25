import sbt._


object Modules {

  val sparkVersion = "2.4.3"

  // Need to be able to specify dependency scope dynamically
  val sparkCore = "org.apache.spark" %% "spark-core" % sparkVersion % Provided
  val sparkSql = "org.apache.spark" %% "spark-sql" % sparkVersion % Provided
  val sparkHive = "org.apache.spark" %% "spark-hive" % sparkVersion % Provided
  val sparkAvro = "org.apache.spark" %% "spark-avro" % sparkVersion % Compile

  val scopt = "com.github.scopt" %% "scopt" % "3.7.1" % Compile
  val typesafeConfig = "com.typesafe" % "config" % "1.3.3" % Compile
  val log4j = "log4j" % "log4j" % "1.2.17" % Provided
  val scalalog = "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2" % Compile
  val scalaTest = "org.scalatest" %% "scalatest" % "3.0.3" % Test
  val scalaMock = "org.scalamock" %% "scalamock" % "4.1.0" % Test

  val avro = "org.apache.avro" % "avro" % "1.8.2" % Compile
  val avro4s = "com.sksamuel.avro4s" %% "avro4s-core" % "3.0.0-RC2" % Compile
  val doobieCore = "org.tpolecat" %% "doobie-core" % "0.8.0-RC1" % Compile

}
