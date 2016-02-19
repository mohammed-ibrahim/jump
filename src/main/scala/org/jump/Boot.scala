package org.jump

import com.typesafe.scalalogging._
import org.slf4j.LoggerFactory

import org.ini4j.Ini
import java.io.FileReader
import scala.collection.JavaConversions._
import java.util.Date

import java.io.StringWriter
import java.io.PrintWriter
import java.io.File

import org.jump.factory._
import org.jump.manager._
import org.jump.db._
import org.jump.common._

object Boot {
  val log = Logger(LoggerFactory.getLogger(this.getClass))
  def main(args: Array[String]): Unit = {

    try {
      var fileName = "sample.ini"
      if (args.length == 1) {
        fileName = args(0)
      }

      val file = new File(fileName)
      if (!file.exists()) {
        throw new RuntimeException(s"The configuration file [${fileName}] doesn't exists or is not accesssible to the application.")
      }

      IniManager.load(fileName)
      DBManager.init

      IniManager.ini.keySet.map { x =>
        runSection(x)
      }
      DBManager.commitAndClose
      log.info("Successfully completed: [COMMITED CHANGES]")
    } catch {
      case e: Exception => {
        DBManager.rollbackAndClose
        val sw = new StringWriter
        e.printStackTrace(new PrintWriter(sw))
        log.error(sw.toString)
        log.error("FAILED! [ROLLED BACK CHANGES]")
      }
    }
  }

  def runSection(sectionTag: String): Unit = {
    val runType = IniManager.getKey(sectionTag, "type")
    val startAt = (new Date).getTime
    val logSql = if (IniManager.getKey("db", "log_sql") == "true") true else false

    if (runType == "insert") {
      var fields = FieldFactory.build(sectionTag)
      var sqlList = SqlBuilder.buildInsert(sectionTag, fields)

      var batchNum = 0
      sqlList.map { x =>
        if (logSql) {
          log.info("Executing the sql: " + x)
        }

        batchNum = batchNum + 1
        log.info(s"Processed batch ${batchNum} with limit ${AppConfig.conf.getInt("batch_size")}");
        DBManager.executeInsert(x)
      }
      log.info(s"[${sectionTag}] imported ${IniManager.getKey(sectionTag, "rows")} rows")
    }

    val endAt = (new Date).getTime

    log.info(s"[${sectionTag}] total time: ${(endAt - startAt).toString} ms")
    System.gc
  }
}
