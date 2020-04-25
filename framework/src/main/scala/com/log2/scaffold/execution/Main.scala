package com.log2.scaffold.execution

import com.typesafe.scalalogging.LazyLogging
import org.apache.spark.sql.SparkSession

abstract class Main[P <: Params] extends LazyLogging {

  protected def run(implicit context: RunContext[P]): Unit

  protected def buildParams(args: Array[String]): P

  protected def sparkBuilderExtras: (SparkSession.Builder, RunContext[P]) => SparkSession.Builder = (builder, _) => builder.appName(this.getClass.getSimpleName)

  protected def createContext(params: P): RunContextImpl[P] =
    new RunContextImpl[P](params, sparkBuilderExtras)

  /**
   * here is the actual main entry, do three things
   * 1 - build the params based on arguments
   * 2 - build the context based on params
   * 3 - call the run method with a implicit [[RunContext]]
   * @param args
   */
  final def main(args: Array[String]): Unit = {
    val params = buildParams(args)
    logger.info(s"Parameters: $params")

    val context = createContext(params)
    run(context)
  }

}
