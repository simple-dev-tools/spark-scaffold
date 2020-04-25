import Modules._

object Dependencies {

  val testDependencies = Seq(
    scalaTest,
    scalaMock
  )

  val libDependencies = Seq(
    sparkCore,
    sparkSql,
    sparkAvro,
    sparkHive,
    scopt,
    typesafeConfig,
    log4j,
    scalalog
  )

  val toolsDependencies = Seq(
    avro,
    avro4s,
    doobieCore,
    typesafeConfig
  )

}
