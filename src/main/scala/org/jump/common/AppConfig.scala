package com.example

import com.typesafe.scalalogging._
import org.slf4j.LoggerFactory
import java.sql._
import com.typesafe.config.ConfigFactory

import org.ini4j.Ini
import java.io.FileReader

object AppConfig {
  val log = Logger(LoggerFactory.getLogger(this.getClass))
  val conf = ConfigFactory.load()
}
