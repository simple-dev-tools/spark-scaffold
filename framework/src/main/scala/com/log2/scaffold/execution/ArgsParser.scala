package com.log2.scaffold.execution

import com.typesafe.scalalogging.LazyLogging
import scopt.OptionParser

import com.log2.scaffold.constants.Env._

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
}
