package com.log2.scaffold.utils

import com.log2.scaffold.test.ContextSuite
import org.apache.spark.sql.Row
import org.apache.spark.sql.functions._
import org.scalatest.{FlatSpec, Matchers}
import java.sql.Date

class BuiltinFunctionTest extends FlatSpec with Matchers with ContextSuite {

  it should "Date related function should work" in {

    val spark = getOrCreateSparkSession
    import spark.implicits._

    val dt1 = "2020-04-05"
    val dt2 = "2020-08-09"

    val df = Seq(
      (1,dt1),
      (2,dt2)
    ).toDF("id","s")

    val df1 = df.withColumn("dt", to_date(col("s")))
      .withColumn("dt1", date_add(col("dt"), 1))
      .withColumn("fmt",date_format(col("dt1"), "yyyyMMdd"))

    val expected = Seq(
      Row(1,dt1, Date.valueOf(dt1), Date.valueOf("2020-04-06"),"20200406"),
      Row(2,dt2, Date.valueOf(dt2), Date.valueOf("2020-08-10"),"20200810")
    )

    checkAnswer(df1, expected)

  }

  it should "correct for array and array_contains" in {
    val spark = getOrCreateSparkSession
    import spark.implicits._

    val df = Seq(
      ("1",1,1),
      ("2",2,2)
    ).toDF("id","n1","n2")

    val df1 = df.withColumn("items", array("n1", "n2"))

    checkAnswer(df1, Seq(
      Row("1",1,1,Array(1,1)),
      Row("2",2,2,Array(2,2))
    ))

    val df2 = df1.withColumn("has_one", array_contains(col("items"), 1))

    checkAnswer(df2, Seq(
      Row("1",1,1,Array(1,1), true),
      Row("2",2,2,Array(2,2), false)
    ))
  }

  it should "have default column name for agg()" in {
    val spark = getOrCreateSparkSession
    import spark.implicits._

    val df = Seq(
      (1,1,1),
      (1,1,3),
      (2,2,2)
    ).toDF("id","n1","n2")

    val df1 = df.groupBy("id").sum()

    df1.columns shouldBe Array("id","sum(id)","sum(n1)","sum(n2)")

    val df2 = df.groupBy("id").agg(
        sum("n1").as("sum_n1"),
        avg("n2").alias("avg_n2") )

    df2.columns shouldBe Array("id","sum_n1","avg_n2")
  }

  it should "join on same column and drop one of it" in {
    val spark = getOrCreateSparkSession
    import spark.implicits._

    val df1 = Seq(
      (1,1),
      (2,2)
    ).toDF("id1","id2")

    val df2 = Seq(
      (3,1),
      (2,2)
    ).toDF("id2","id3")

    val df3 = df1.join(df2, df1("id2") === df2("id2"), "left_outer").drop(df1("id2"))

    df3.columns shouldBe Array("id1","id2","id3")

    checkAnswer(
      df3,
      Seq(
        Row(1,null,null),
        Row(2,2,2)
      )
    )
  }
}