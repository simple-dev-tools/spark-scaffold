package com.log2.scaffold.execution

trait Params {

  def env: String
  def env_=(value: String): Unit

  def debug: Boolean
  def debug_=(value: Boolean): Unit

  override def toString: String =
    s"""
      |/n
      |env = $env
      |debug = $debug
      |additionalParams = ${super.toString}
      |/n
      |""".stripMargin

}
