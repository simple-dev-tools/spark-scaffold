package com.log2.scaffold.execution

import java.sql.Date

import com.log2.scaffold.constants.Env

case class CsvJobParam(override var env: String = Env.Default,
                       override var debug: Boolean = false,
                       override var paramDate: Date = null,
                       override var paramInt: Int = 0,
                       override var paramString: String = "",
                       var csvFilePath: String = "") extends Params {
  override def toString: String =
    getClass.getDeclaredFields
      .map(_.getName)
      .zip(productIterator.to)
      .map{ case (name, value) => f"$name: $value" }
      .mkString("\n")
}


