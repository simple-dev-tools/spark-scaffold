package com.log2.scaffold.utils


import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.functions.{lit,expr}
import org.apache.spark.sql.SparkSession

import java.sql.Date

object Helper {

  def buildDateRange(columnName: String, startDate: Date, numberOfDays: Int)
                    (implicit spark: SparkSession): DataFrame = {
    spark.range(numberOfDays).toDF().withColumn("start_date", lit(startDate))
      .withColumn(columnName, expr("date_add(start_date, id)"))
      .select(columnName)
  }

}
