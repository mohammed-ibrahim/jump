package com.example

import com.typesafe.scalalogging._
import org.slf4j.LoggerFactory

import org.ini4j.Ini
import java.io.FileReader

object ConfigParser {
  val log = Logger(LoggerFactory.getLogger(this.getClass))

  def parseImportStatement(sectionTag: String): List[(String, String)] = {
    val fields = ConfigManager.getKey(sectionTag, "stragegy")

    val result = fields.split(",").map { x =>
      val item = x.split(":").toList
      (item(0).trim, item(1).trim)
    }.toList

    result
  }
}
