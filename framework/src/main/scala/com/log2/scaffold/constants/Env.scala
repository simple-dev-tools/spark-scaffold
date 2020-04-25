package com.log2.scaffold.constants

object Env {
  val Local = "local"
  val Dev = "dev"
  val Qa = "qa"
  val Prod = "prod"
  def Default = sys.env.getOrElse("env", Local)
}
