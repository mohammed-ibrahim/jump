package com.example

import com.typesafe.scalalogging._
import org.slf4j.LoggerFactory

import org.ini4j.Ini
import java.io.FileReader

object ConfigParser {
  val log = Logger(LoggerFactory.getLogger(this.getClass))

  def parseInsert(sectionTag: String): List[(String, String, String)] = {
    val fields = ConfigManager.getKey(sectionTag, "fields")

    if (fields.trim.length < 1) {
      throw new RuntimeException(s"The property [fields] are not mentioned properly in section [${sectionTag}]")
    }

    fields.split(",").map { x =>
      var v = parseField(x)
      (v(0), v(1), v(2))
    }.toList
  }

  private def parseField(field: String): List[String] = {
    var parsed = field.replace("=", "#").replace("(", "#").replace(")", "#").split("#").map(x => x.trim).toList

    if (parsed.length != 3) {
      throw new RuntimeException("The fields are not properly mentioned, parameters are supposed to be in format: fiend_name = func(param)")
    }

    parsed
  }
}
