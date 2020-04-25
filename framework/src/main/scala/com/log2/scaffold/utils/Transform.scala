package com.log2.scaffold.utils

import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.functions.lit

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

}
