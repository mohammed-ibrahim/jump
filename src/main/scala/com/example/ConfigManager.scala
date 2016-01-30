package com.example

import com.typesafe.scalalogging._
import org.slf4j.LoggerFactory
import java.sql._

import org.ini4j._
import org.ini4j.Ini._
import java.io.FileReader
import scala.collection.mutable.Map

object ConfigManager {
  val log = Logger(LoggerFactory.getLogger(this.getClass))
  var ini: Ini = null

  def load(fileName: String): Unit = {
    this.ini = new Ini(new FileReader(fileName))
  }

  def getStartSectionKey(key: String): String = {
    ini.get("start").fetch(key)
  }

  def getSectionKey(tag: String, key: String): String = {
    ini.get(tag).fetch(key)
  }
}
