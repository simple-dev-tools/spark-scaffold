package com.log2.scaffold.utils

import org.scalatest.{FlatSpec, Matchers}
import org.apache.spark.sql.Row
import com.log2.scaffold.utils.Transform._
import com.log2.scaffold.test.ContextSuite

import java.sql.Date

class TransformTest extends FlatSpec with Matchers with ContextSuite {

  it should "add column with given value" in {

    val spark = getOrCreateSparkSession
    import spark.implicits._

    val df = Seq(
      ("String1",1),
      ("String2",2)
    ).toDF("c1","c2")

    val result = df.transform(addColumn("c3","v"))

    val expected = Seq(
      Row("String1",1,"v"),
      Row("String2",2,"v")
    )

    /**
     * check columns first
     */
    result.columns shouldBe Array("c1","c2","c3")

    /**
     * check the value
     */
    checkAnswer(result, expected)
  }

  it should "create a df with range range" in {

    val spark = getOrCreateSparkSession

    val startDate = Date.valueOf("2020-01-01")

    val idDF = spark.range(10).toDF()

    val result = idDF.transform(withDateRange("my_date", startDate))

    result.columns shouldBe Array("my_date")

    val expected = Seq(
      Row(Date.valueOf("2020-01-01")),
      Row(Date.valueOf("2020-01-02")),
      Row(Date.valueOf("2020-01-03")),
      Row(Date.valueOf("2020-01-04")),
      Row(Date.valueOf("2020-01-05")),
      Row(Date.valueOf("2020-01-06")),
      Row(Date.valueOf("2020-01-07")),
      Row(Date.valueOf("2020-01-08")),
      Row(Date.valueOf("2020-01-09")),
      Row(Date.valueOf("2020-01-10"))
    )

    checkAnswer(result, expected)

  }
}