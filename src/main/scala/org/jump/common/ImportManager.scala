package org.jump.common

import java.io.FileReader
import scala.collection.JavaConversions._

import org.jump.entity._
import org.jump.manager._
import org.jump.logging._
import org.jump.db._

import org.jump.parser.SqlCommand
import org.jump.parser.InsertCommand

object ImportManager {
  val log = LogManager.createInstance(this.getClass.getName)

  def process(commandNum: String, command: InsertCommand, fields: List[Field]): Unit = {
    val numRows = command.getNumRows
    val batchSize = AppConfig.conf.getInt("batch_size")

    var logSql: Boolean = false
    if (AppConfig.conf.getString("log_sql") == "true") {
      logSql = true
    }

    if (numRows < 1) {
      throw new RuntimeException("[rows] should be atleast 1")
    }

    val totalIterations = numRows / batchSize
    val remaining = numRows % batchSize

    val tableName = command.getTableName
    var total = 0

    for (i <- 0 until totalIterations) {
      val sql = buildBatch(fields, batchSize, tableName)
      if (logSql) {
        log.info("Sql: " + sql);
      }
      DBManager.execute(sql)
      total = total + batchSize
      log.info(s"[${commandNum}] Inserted [${total}] rows")
    }

    if (remaining > 0) {
      val sql = buildBatch(fields, remaining, tableName)
      if (logSql) {
        log.info("Sql: " + sql);
      }
      DBManager.execute(sql)
      total = total + remaining
      log.info(s"[${commandNum}] Inserted [${total}] rows")
    }
  }

  private def buildBatch(fields: List[Field], rows: Int, tableName: String): String = {
    val container = new java.util.ArrayList[String]

    for (i <- 0 until rows) {
      container.add(buildRow(fields))
    }

    var finalSql = "insert into " + tableName
    finalSql += "(" + fields.map (x => x.getName).toList.mkString(",") + ") values "
    finalSql += container.toList.mkString(",")

    finalSql
  }

  private def buildRow(fields: List[Field]): String = {
    val builder = new StringBuilder
    var prefix = ""

    builder.append("(")

    fields.map { x =>
      builder.append(prefix)
      builder.append(x.produce)
      prefix = ","
    }

    builder.append(")")
    builder.toString
  }
}
