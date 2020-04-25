package com.log2.scaffold.execution

import java.sql.Date

trait Params {

  def env: String
  def env_=(value: String): Unit

  def debug: Boolean
  def debug_=(value: Boolean): Unit

  def paramDate: Date
  def paramDate_=(value: Date): Unit

  def paramInt: Int
  def paramInt_=(value: Int): Unit

  def paramString: String
  def paramString_=(value: String): Unit

  override def toString: String =
    s"""
      |env = $env
      |debug = $debug
      |paramDate = $paramDate
      |paramInt = $paramInt
      |paramString = $paramString
      |additionalParams = ${super.toString}
      |""".stripMargin

}
