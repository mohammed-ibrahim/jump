package org.jump.factory

import java.io.FileReader
import scala.collection.JavaConversions._

import org.jump.parser._
import org.jump.entity._
import org.jump.db._
import org.jump.common._

import org.jump.parser.SqlCommand
import org.jump.parser.InsertCommand

object FieldFactory {
  def build(command: InsertCommand): List[Field] = {
    var fields = command.getFieldConfigs

    var index = 0
    fields.map { x =>
      index = index + 1
      val commandNumber = "Field: " + index.toString
      buildField(commandNumber, x)
    }.toList
  }

  def buildField(commandNumber: String, field: FieldConfig): Field = {
    var crawlerTypes = Set("one_of", "static", "serial", "sql", "between", "random_between")
    var supported = crawlerTypes ++ Set("fake") ++ Set("now")

    if (!supported.contains(field.getFnName)) {
      throw new RuntimeException(s"Unknown function [${field.getFnName}]")
    }

    if (crawlerTypes.contains(field.getFnName)) {
      return new CrawlerField(field, buildCrawler(commandNumber, field))
    } else if (field.getFnName == "fake") {
      if (field.getParams.size != 1) {
        throw new RuntimeException("The function [fake] takes only 1 parameter")
      }

      return new FakerField(field)
    } else if (field.getFnName == "now") {
      return new NowField(field)
    }

    throw new RuntimeException(s"Unknown function [${field.getFnName}]")
  }

  private def buildCrawler(commandNumber: String, field: FieldConfig): Crawler = {
    field.getFnName match {
      case "random_between" => buildRandomBetweenCrawler(commandNumber, field)

      case "between"        => buildBetweenCrawler(commandNumber, field)

      case "one_of"         => buildOneOfCrawler(commandNumber, field)

      case "static"         => buildStaticCrawler(commandNumber, field)

      case "serial"         => buildSerialCrawler(commandNumber, field)

      case "sql"            => buildSqlCrawler(commandNumber, field)

      case _ => {
        throw new RuntimeException("Unknown function " + field.getFnName)
      }
    }
  }

  def isDigit(input: String): Boolean = {
    if (input.size == 0) {
      return false
    }

    val len = input.filter (x => Character.isDigit(x)).size

    if (len == input.size) {
      return true
    }

    return false
  }

  private def buildSqlCrawler(commandNumber: String, field: FieldConfig): Crawler = {
    val params = field.getParams.toList
    if (!Set(1, 2).contains(params.size)) {
      val msg1 = s"The method [sql] can be called with either 1 or 2 parameters: Field [${commandNumber}]"
      val msg2 = "1. [sql(<sql_query>)] or 2. [sql(<sql_query>, int)]"
      throw new RuntimeException(msg1 + msg2)
    }

    var sql = params(0)
    var numInterations = 1

    if (params.size == 2) {
      if (isDigit(params(1))) {
        val iter = params(1).toInt
        if (iter > 0) {
          numInterations = iter
        }
      } else {
        throw new RuntimeException("Param 2 of the method sql should be an integer")
      }
    }

    val avList = DBManager.getAvList(sql)

    new Crawler(avList, "serial", numInterations, commandNumber)
  }

  private def buildStaticCrawler(commandNumber: String, field: FieldConfig): Crawler = {
    if (field.getParams.size != 1) {
      throw new RuntimeException(s"The method static takes only 1 parameter: Error in Field [${commandNumber}] Under the key fields")
    }

    new Crawler(field.getParams.toList, "serial", 1, commandNumber)
  }

  private def buildSerialCrawler(commandNumber: String, field: FieldConfig): Crawler = {
    new Crawler(field.getParams.toList, "serial", 1, commandNumber)
  }

  private def buildOneOfCrawler(commandNumber: String, field: FieldConfig): Crawler = {
    if (field.getParams.size < 1) {
      throw new RuntimeException(s"Not enough params for the function one_of in Field [${commandNumber}] Under the key fields")
    }

    new Crawler(field.getParams.toList, "random", 1, commandNumber)
  }

  private def buildBetweenCrawler(commandNumber: String, field: FieldConfig): Crawler = {
    if (field.getParams.size != 2) {
      throw new RuntimeException(s"The function [between] takes two parameters [between(int, int)], Field [${commandNumber}]")
    }

    var v1 = field.getParams.get(0)
    var v2 = field.getParams.get(1)

    if (!isDigit(v1) || !isDigit(v2)) {
      throw new RuntimeException(s"The function [between] requires both parameters to be int [between(int, int)], Field [${commandNumber}]")
    }

    var start = Math.min(v1.toInt, v2.toInt)
    var end = Math.max(v1.toInt, v2.toInt)

    if (start == end) {
      throw new RuntimeException(s"The function [between] parameter 1 should be greater than parameter 2 [between(int, int)], Field [${commandNumber}]")
    }

    new Crawler((start to end).toList.map (x => x.toString).toList, "serial", 1, commandNumber)
  }

  private def buildRandomBetweenCrawler(commandNumber: String, field: FieldConfig): Crawler = {
    if (field.getParams.size != 2) {
      throw new RuntimeException(s"The function [between] takes two parameters [between(int, int)], Field [${commandNumber}]")
    }

    var v1 = field.getParams.get(0)
    var v2 = field.getParams.get(1)

    if (!isDigit(v1) || !isDigit(v2)) {
      throw new RuntimeException(s"The function [between] requires both parameters to be int [between(int, int)], Field [${commandNumber}]")
    }

    var start = Math.min(v1.toInt, v2.toInt)
    var end = Math.max(v1.toInt, v2.toInt)

    if (start == end) {
      throw new RuntimeException(s"The function [between] parameter 1 should be greater than parameter 2 [between(int, int)], Field [${commandNumber}]")
    }

    new Crawler((start to end).toList.map (x => x.toString).toList, "random", 1, commandNumber)
  }
}
