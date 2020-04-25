package com.log2.scaffold.execution

import com.typesafe.scalalogging.LazyLogging
import org.apache.spark.sql.SparkSession

abstract class Main[P <: Params] extends LazyLogging {

  protected def run(implicit context: RunContext[P]): Unit

  protected def buildParams(args: Array[String]): P

  protected def sparkBuilderExtras: (SparkSession.Builder, RunContext[P]) => SparkSession.Builder = (builder, _) => builder.appName(this.getClass.getSimpleName)

  protected def createContext(params: P): RunContextImpl[P] =
    new RunContextImpl[P](params, sparkBuilderExtras)

}
