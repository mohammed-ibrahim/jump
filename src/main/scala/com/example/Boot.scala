package com.example

import com.typesafe.scalalogging._
import org.slf4j.LoggerFactory

import org.ini4j.Ini
import java.io.FileReader
import scala.collection.JavaConversions._
import java.util.Date

import java.io.StringWriter
import java.io.PrintWriter

object Boot {
  val log = Logger(LoggerFactory.getLogger(this.getClass))
  def main(args: Array[String]): Unit = {

    try {
      var fileName = "sample.ini"
      if (args.length == 1) {
        fileName = args(0)
      }

      ConfigManager.load(fileName)
      ConfigManager.ini.keySet.map { x =>
        runSection(x)
      }
      DBManager.closeConn(false)
      log.info("Successfully completed: commiting changes")
    } catch {
      case e: Exception => {
        DBManager.closeConn(true)
        val sw = new StringWriter
        e.printStackTrace(new PrintWriter(sw))
        log.error("Failed with error: rolling back: " + e.toString)
      }
    }
  }

  def runSection(sectionTag: String): Unit = {
    val runType = ConfigManager.getKey(sectionTag, "type")
    val startAt = (new Date).getTime

    if (runType == "import") {
      var result = ConfigParser.parseImportStatement(sectionTag)
      var generatedSql = SqlBuilder.buildImportStatement(sectionTag, result)
      var total = 0
      generatedSql.map { x =>
        DBManager.executeInsert(x)
        total = total + x.length
      }

      log.info(s"[${sectionTag}] imported ${ConfigManager.getKey(sectionTag, "rows")} rows")
    }

    val endAt = (new Date).getTime

    log.info(s"[${sectionTag}] total time: ${(endAt - startAt).toString} ms")
    System.gc
  }
}
