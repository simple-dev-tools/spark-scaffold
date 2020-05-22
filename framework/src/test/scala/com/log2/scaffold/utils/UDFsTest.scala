package com.log2.scaffold.utils

import com.log2.scaffold.test.ContextSuite
import com.log2.scaffold.utils.UDFs.getDateRange
import org.apache.spark.sql.Row
import org.apache.spark.sql.functions._
import org.scalatest.{FlatSpec, Matchers}
import java.sql.Date

import org.apache.spark.sql.types.{ArrayType, DateType}

class UDFsTest extends FlatSpec with Matchers with ContextSuite {

  it should "create correct date range by dateRangeUDF" in {

    val spark = getOrCreateSparkSession
    import spark.implicits._

    val dateRangeUDF = udf(getDateRange _, ArrayType(DateType))

    val startDate = Date.valueOf("2020-01-01")
    val middleDate = Date.valueOf("2020-01-02")
    val endDate = Date.valueOf("2020-01-03")

    val df = Seq(
      (startDate,endDate)
    ).toDF("start_date","end_date")

    val df1 = df.withColumn("range", dateRangeUDF(col("start_date"), col("end_date")))
      .withColumn("date", explode(col("range")))
      .drop("range")


    val expected = Seq(
      Row(startDate,endDate,startDate),
      Row(startDate,endDate,middleDate),
      Row(startDate,endDate,endDate)
    )
    checkAnswer(df1, expected)
  }


}