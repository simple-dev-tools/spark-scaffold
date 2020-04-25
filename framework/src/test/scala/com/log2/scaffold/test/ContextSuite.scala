package com.log2.scaffold.test

import com.typesafe.scalalogging.LazyLogging
import org.apache.hadoop.fs.{FileSystem, Path}
import org.apache.spark.SparkConf
import org.apache.spark.sql.{QueryTest, SparkSession}
import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach, Suite}
import java.sql.Date

import com.log2.scaffold.config.AppConfig
import com.log2.scaffold.constants.Env
import com.log2.scaffold.execution.{Params, RunContext, RunContextImpl}

trait ContextSuite extends QueryTest with BeforeAndAfterEach with BeforeAndAfterAll with LazyLogging {
  self: Suite =>

  protected def getOrCreateSparkSession: SparkSession = {
    val config = AppConfig.config
    val conf = new SparkConf()
      .setAppName(this.getClass.getSimpleName)
      .set("spark.sql.sources.partitionColumnTypeInference.enabled", "true")
      .set("hive.exec.dynamic.partition", "true")
      .set("hive.exec.dynamic.partition.mode", "nonstrict")
      .set("spark.sql.sources.partitionOverwriteMode", "STATIC")
      .set("spark.sql.crossJoin.enabled", "true")
      .set("spark.sql.shuffle.partitions", config.getString("hive.bucket_num.default"))
      .set("spark.sql.autoBroadcastJoinThreshold", "-1")
      .set("spark.sql.codegen.wholeStage", "true")
      .setMaster("local")

    val spark = withExtensions(SparkSession.builder())
      .enableHiveSupport()
      .config(conf)
      .getOrCreate()

    spark.sparkContext.setCheckpointDir(config.getString("spark.checkpointDir"))

    spark
  }

  protected def withExtensions(builder: SparkSession.Builder): SparkSession.Builder = builder

  protected def createContext[P <: Params](params: P, sparkSession: Option[SparkSession] = None): RunContext[P] = {
    new RunContextImpl[P](params) {
      override lazy val spark: SparkSession = getOrCreateSparkSession

      override def dispose: Unit = {
        /**
         * Do nothing dispose. In unit test spark session are reused, if we dispose the spark session, session will
         * be dropped but the derby instance will yet be there and not being reusable in the next test.
         */
      }
    }
  }

  override protected def beforeEach(): Unit = {
    val cleanupRunContext = new RunContextImpl[Params](new Params {
      override def env: String = Env.Local

      override def env_=(value: String): Unit = ???

      override def debug: Boolean = false

      override def debug_=(value: Boolean): Unit = ???

      override def paramDate: Date = ???

      override def paramDate_=(value: Date): Unit = ???

      override def paramInt: Int = ???

      override def paramInt_=(value: Int): Unit = ???

      override def paramString: String = ???

      override def paramString_=(value: String): Unit = ???

    }) {
      override def spark: SparkSession = getOrCreateSparkSession
    }

    cleanupRunContext.spark.catalog.listDatabases().collect()
      .filter(_.name != "default")
      .foreach(db => cleanupRunContext.spark.sql(s"DROP DATABASE IF EXISTS ${db.name} CASCADE"))

    val stagingRoot = cleanupRunContext.appConfig.getString("staging.root")
    val fs = FileSystem.newInstance(cleanupRunContext.spark.sparkContext.hadoopConfiguration)
    fs.delete(new Path(stagingRoot), true)
  }

}
