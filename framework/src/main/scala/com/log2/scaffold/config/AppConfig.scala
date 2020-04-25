package com.log2.scaffold.config

import com.typesafe.config.{Config, ConfigFactory}
import com.typesafe.scalalogging.LazyLogging

import scala.collection.mutable

object AppConfig extends LazyLogging {
  private val configs = mutable.Map[String, Config]()

  private var _config: Config = _

  /**
   * Initialize the default configuration.
   *
   * @param env The environment key, either "local", "dev", "qa", "prod".
   */
  def init(env: String) = {
    _config = configOf(env)
  }

  def config = {
    if (_config == null) {
      throw new RuntimeException("Default configuration is not initialized, should initialize it in App startup.")
    }
    _config
  }

  /**
   * Get the configuration of an environment.
   *
   * @param env The environment key, either "local", "dev", "qa", "prod".
   * @return The [[Config]] object.
   */
  private def configOf(env: String): Config = {
    configs.getOrElseUpdate(env, {
      val resourcePath = s"configs/${env}.conf"
      logger.info(s"Loading $env config from $resourcePath")
      ConfigFactory.parseResources(resourcePath)
    })
  }


}
