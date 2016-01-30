package com.example

import com.typesafe.scalalogging._
import org.slf4j.LoggerFactory

import org.ini4j.Ini
import java.io.FileReader

object SqlBuilder {
  val log = Logger(LoggerFactory.getLogger(this.getClass))

  def buildImportStatement(params: List[(String, String)], rows: Int): String = {
    val builder = new StringBuilder
    var prefix = ""

    builder.append("(")
    params.map {
      case (key: String, value: String) => {
        val item = buidItem(key, value)
        log.info(s"Generated item key: ${key} value: ${value} = ${item}")
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
    strategy match {
      case "fake" => {
        return FFWrapper.getFake(param)
      }

      case "static" => {
        return param
      }

      case "av" => {
        return AVManager.getRandom(param)
      }

      case _ => {
        throw new RuntimeException(s"Unsupported stragegy strategy type: ${strategy}")
      }
    }
  }
}
