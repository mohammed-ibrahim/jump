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

  def assertSectionExists(sectionTag: String) = {
    var section = ini.get(sectionTag)

    if (section == null) {
      throw new RuntimeException(s"Config section not found[${sectionTag}], This section is mandatory")
    }
  }

  def assertSectionExistsWithKeys(sectionTag: String, keys: List[String]) = {

    null
  }

  def getKey(sectionTag: String, key: String): String = {
    var section = ini.get(sectionTag)
    if (section == null) {
      throw new RuntimeException(s"Config section not found[${sectionTag}]")
    }

    var result = ini.get(sectionTag).fetch(key)
    if (result == null) {
      throw new RuntimeException(s"Config key [${key}] not found in section [${sectionTag}]")
    }

    result
  }
}
