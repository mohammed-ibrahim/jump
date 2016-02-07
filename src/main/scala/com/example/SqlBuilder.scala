package com.example

import com.typesafe.scalalogging._
import org.slf4j.LoggerFactory

import org.ini4j.Ini
import java.io.FileReader
import scala.collection.JavaConversions._

object SqlBuilder {
  val log = Logger(LoggerFactory.getLogger(this.getClass))

  def buildInsert(sectionTag: String, params: List[(String, String, String)]): List[String] = {
    val numRows = ConfigManager.getKey(sectionTag, "rows").toInt
    val batchSize = AppConfig.conf.getInt("batch_size")

    val totalIterations = numRows / batchSize
    val remaining = numRows % batchSize

    val tableName = ConfigManager.getKey(sectionTag, "table")

    val builder = new java.util.ArrayList[String]
    for (i <- 0 until totalIterations) {
      builder.add(buildBatch(params, batchSize, tableName))
    }

    if (remaining > 0) {
      builder.add(buildBatch(params, remaining, tableName))
    }

    builder.toList
  }

  private def buildBatch(params: List[(String, String, String)], rows: Int, tableName: String): String = {
    val container = new java.util.ArrayList[String]

    for (i <- 0 until rows) {
      container.add(buildRow(params))
    }

    var finalSql = "insert into " + tableName
    finalSql += "(" + params.map (x => x._1).toList.mkString(",") + ") values "
    finalSql += container.toList.mkString(",")

    finalSql
  }

  private def buildRow(params: List[(String, String, String)]): String = {
    val builder = new StringBuilder
    var prefix = ""

    builder.append("(")
    params.map {
      case (field: String, func: String, func_param: String) => {
        val item = buidItem(func, func_param)
        builder.append(prefix)
        builder.append(item)
        prefix = ","
      }
    }
    builder.append(")")

    builder.toString
  }

  private def buidItem(func: String, param: String): String = {
    var str = ""

    func match {
      case "fake" => {
        str = wrap(escape(FakeManager.custom(param)))
      }

      case "static" => {
        str = param
      }

      case "section" => {
        str = wrap(escape(AVManager.getNext(param)))
      }

      case _ => {
        throw new RuntimeException(s"Unsupported function: ${func}")
      }
    }

    str
  }

  private def wrap(term: String): String = {
    "'" + term + "'"
  }

  private def escape(term: String): String = {
    term.replace("'", "\\'")
  }
}
