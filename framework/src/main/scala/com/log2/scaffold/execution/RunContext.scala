package com.log2.scaffold.execution

import com.typesafe.config.Config
import org.apache.spark.sql.SparkSession

trait RunContext[+P <: Params] {
  def spark: SparkSession

  def params: P

  def appConfig: Config

  def dispose(): Unit

}
