package com.example

import com.typesafe.scalalogging._
import org.slf4j.LoggerFactory

import org.ini4j.Ini
import java.io.FileReader
import scala.collection.JavaConversions._
import java.util.Date

object Boot {
  val log = Logger(LoggerFactory.getLogger(this.getClass))
  def main(args: Array[String]): Unit = {

    var fileName = "sample.ini"
    if (args.length == 1) {
      fileName = args(0)
    }

    ConfigManager.load(fileName)
    ConfigManager.ini.keySet.map { x =>
      runSection(x)
    }

    DBManager.closeConn
  }

  def runSection(sectionTag: String): Unit = {
    val runType = ConfigManager.getSectionKey(sectionTag, "type")
    val startAt = (new Date).getTime

    if (runType == "import") {
      var result = ConfigParser.parseImportStatement(sectionTag)
      var generatedSql = SqlBuilder.buildImportStatement(sectionTag, result)
      generatedSql.map { x =>
        DBManager.executeInsert(x)
      }
    }

    val endAt = (new Date).getTime

    log.info(s"Total time taken to execute [${sectionTag}] was: ${(endAt - startAt).toString} ms")
  }
}
