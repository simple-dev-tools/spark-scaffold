package com.log2.scaffold.utils

import java.sql.Date

import com.log2.scaffold.test.ContextSuite
import com.log2.scaffold.utils.Helper._
import org.apache.spark.sql.Row
import org.scalatest.{FlatSpec, Matchers}

class HelperTest extends FlatSpec with Matchers with ContextSuite {

  it should "add column with given value" in {

//    val spark = getOrCreateSparkSession

    val startDate = Date.valueOf("2019-12-28")
    val numberOfDays = 6

    val result = buildDateRange("zzz", startDate, numberOfDays)(getOrCreateSparkSession)

    val expected = Seq(
      Row(Date.valueOf("2019-12-28")),
      Row(Date.valueOf("2019-12-29")),
      Row(Date.valueOf("2019-12-30")),
      Row(Date.valueOf("2019-12-31")),
      Row(Date.valueOf("2020-01-01")),
      Row(Date.valueOf("2020-01-02"))
    )
    result.columns shouldBe Array("zzz")

    checkAnswer(result, expected)
  }
}