package com.example

import com.typesafe.scalalogging._
import org.slf4j.LoggerFactory

import org.ini4j.Ini
import java.io.FileReader
import scala.collection.JavaConversions._

object SqlBuilder {
  val log = Logger(LoggerFactory.getLogger(this.getClass))

  def buildImportStatement(sectionTag: String, params: List[(String, String)]): List[String] = {
    val numRows = ConfigManager.getSectionKey(sectionTag, "rows").toInt
    val batchSize = AppConfig.conf.getInt("batch_size")

    val totalIterations = numRows / batchSize
    val remaining = numRows % batchSize

    log.info(s"Running total of ${totalIterations} iteration(s) with ${remaining}")
    val tableName = ConfigManager.getSectionKey(sectionTag, "table")
    val fields = ConfigManager.getSectionKey(sectionTag, "fields")

    val builder = new java.util.ArrayList[String]
    for (i <- 0 until totalIterations) {
      builder.add(buildBatch(params, batchSize, tableName, fields))
    }

    if (remaining > 0) {
      builder.add(buildBatch(params, remaining, tableName, fields))
    }

    builder.toList
  }

  private def buildBatch(params: List[(String, String)], rows: Int, tableName: String, fields: String): String = {
    val container = new java.util.ArrayList[String]

    for (i <- 0 until rows) {
      container.add(buildRow(params))
    }

    var finalSql = "insert into " + tableName
    finalSql += "(" + fields + ") values "
    finalSql += container.toList.mkString(",")

    finalSql
  }

  private def buildRow(params: List[(String, String)]): String = {
    val builder = new StringBuilder
    var prefix = ""

    builder.append("(")
    params.map {
      case (key: String, value: String) => {
        val item = buidItem(key, value)
        builder.append(prefix)
        builder.append("'")
        builder.append(item)
        builder.append("'")
        prefix = ","
      }
    }
    builder.append(")")

    builder.toString
  }

  private def buidItem(strategy: String, param: String): String = {
    var str = ""

    strategy match {
      case "fake" => {
        str = FakeManager.getFake(param)
      }

      case "static" => {
        str = param
      }

      case "av" => {
        str = AVManager.getRandom(param)
      }

      case _ => {
        throw new RuntimeException(s"Unsupported stragegy strategy type: ${strategy}")
      }
    }

    str.replace("'", "\\'")
  }
}
