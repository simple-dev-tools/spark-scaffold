package com.log2.scaffold.utils

import org.scalatest.{FlatSpec, Matchers}
import org.apache.spark.sql.Row
import com.log2.scaffold.utils.Transform._
import com.log2.scaffold.test.ContextSuite

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
}