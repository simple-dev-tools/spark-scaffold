package com.log2.scaffold.utils

import java.sql.Date
import java.time.Duration

object UDFs {

  /**
   * accept two [[Date]] and return the range of start to end dates
   * @param dateFrom
   * @param dateTo
   * @return [[Seq]]
   */
  def getDateRange(dateFrom: Date, dateTo: Date ): Seq[Date] = {
    val daysBetween = Duration
      .between(
        dateFrom.toLocalDate.atStartOfDay(),
        dateTo.toLocalDate.atStartOfDay()
      )
      .toDays

    val newRows = Seq.newBuilder[Date]
    for (day <- 0L to daysBetween) {
      val date = Date.valueOf(dateFrom.toLocalDate.plusDays(day))
      newRows += date
    }
    newRows.result()
  }


}
