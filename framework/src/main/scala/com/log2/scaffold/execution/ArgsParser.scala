package com.log2.scaffold.execution

import com.typesafe.scalalogging.LazyLogging
import scopt.OptionParser
import java.sql.Date
import java.text.SimpleDateFormat

import com.log2.scaffold.constants.Env._
import com.log2.scaffold.constants.Values._

class ArgsParser[A <: Params](programName: String) extends OptionParser[A](programName) with LazyLogging {

  opt[String]('e', "env")
    .action({ (e, params) =>
      params.env = e
      params
    })
    .text(s"Environment parameter, should be one of $Dev, $Qa, $Prod, $Local")
    .required()

  opt[Boolean]('d', "debug")
    .action({
      (d, params) =>
        params.debug = d
        params
    })
    .text("'true' if run in debug mode, 'false' if normal run.")
    .required()

  opt[String]("p_dt")
    .action({
      (dt, params) =>
        val _dt = new Date(new SimpleDateFormat(DefaultDateFormat).parse(dt).getTime())
        params.paramDate = _dt
        params
    })
    .text(s"The execution date of simulation in ${DefaultDateFormat} format")
    .required()

  opt[Int]("p_int")
    .action ({
      (p_int, params) =>
        params.paramInt = p_int
        params
    })
    .text("The wave number which is a integer and greater than or equal to 1")
    .validate(wn => if (wn >= 1) { success } else {
      failure("The wave_number must be a integer and greater or equal 1")
    })
    .required()

  opt[String]("p_string")
    .action ({
      (p_string, params) =>
        params.paramString = p_string
        params
    })
    .text("The wave number which is a integer and greater than or equal to 1")
    .required()
}
