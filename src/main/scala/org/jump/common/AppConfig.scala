package org.jump.common

import java.sql._
import com.typesafe.config.ConfigFactory

import java.io.FileReader

object AppConfig {
  val conf = ConfigFactory.load()
}
