package com.log2.scaffold.utils

import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.functions.{lit, expr}

import java.sql.Date

object Transform {


  /**
   * Example DataFrame transformation helper, to be used df.transform(addColumn("abc","bcd"))
   * @param columnName
   * @param columnValue
   * @param df
   * @return
   */
  def addColumn(columnName: String, columnValue: String)(df: DataFrame): DataFrame = {
    df.withColumn(columnName, lit(columnValue))
  }

  def withDateRange(columnName: String, startDate: Date)(df: DataFrame): DataFrame = {
      df.withColumn("start_date", lit(startDate))
      .withColumn(columnName, expr("date_add(start_date, id)"))
      .select(columnName)
  }

}
