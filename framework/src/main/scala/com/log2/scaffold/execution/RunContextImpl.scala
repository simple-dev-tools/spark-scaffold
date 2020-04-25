package com.log2.scaffold.execution

import com.typesafe.config.Config
import com.log2.scaffold.config.AppConfig
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

class RunContextImpl[P <: Params](val params: P,
                                  val sparkBuilderExtras: (SparkSession.Builder, RunContext[P]) => SparkSession.Builder = (builder: SparkSession.Builder, _: RunContext[P]) => builder
                                 ) extends RunContext[P] {
  AppConfig.init(params.env)

  override def appConfig: Config = AppConfig.config

  override def dispose: Unit = spark.close()

  override def spark: SparkSession = {
    val conf = new SparkConf()
      .setAppName(this.getClass.getSimpleName)
      .set("spark.sql.sources.partitionColumnTypeInference.enabled", "true")
      .set("hive.exec.dynamic.partition", "true")
      .set("hive.exec.dynamic.partition.mode", "nonstrict")
      .set("spark.sql.sources.partitionOverwriteMode", "STATIC")
      .set("mapreduce.fileoutputcommitter.algorithm.version", "2")
      .set("spark.sql.crossJoin.enabled", "true")
      .set("spark.sql.shuffle.partitions", appConfig.getString("hive.bucket_num.default"))

    val builder = SparkSession.builder()
      .enableHiveSupport()
      .config(conf)

    val spark = sparkBuilderExtras(builder, this)
      .getOrCreate()

    assert(spark.version.startsWith("2.4."), "Supported version of Spark is 2.4.*, if you'd support other version, please check and update all the private changes under org.apache.spark.*")

    spark.sparkContext.setCheckpointDir(appConfig.getString("spark.checkpointDir"))

    spark
  }

}
