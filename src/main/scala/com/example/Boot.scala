package com.example

import com.typesafe.scalalogging._
import org.slf4j.LoggerFactory

import org.ini4j.Ini
import java.io.FileReader

object Boot {
  val log = Logger(LoggerFactory.getLogger(this.getClass))
  def main(args: Array[String]): Unit = {

    var fileName = "sample.ini"
    if (args.length == 1) {
      fileName = args(0)
    }

    ConfigManager.load(fileName)
    val section = ConfigManager.getStartSection

    section.get("seq").split(",").map { x =>
      runSection(ini, x)
    }
  }

  def runSection(sectionTag: String): Unit = {
    val runType = ConfigManager.getSection(sectionTag).fetch("type")
    log.info("Running the type: " + runType)

    if (runType == "import") {
      var result = ConfigParser.parseImportStatement(sectionTag)
      log.info("result is: " + result.toString)

      var generatedSql = SqlBuilder.buildImportStatement(result, 10)
      log.info("Generated sql: " + generatedSql)
    }
  }
}
