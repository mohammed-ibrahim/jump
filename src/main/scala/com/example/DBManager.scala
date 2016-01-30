package com.example

import com.typesafe.scalalogging._
import org.slf4j.LoggerFactory
import java.sql._

import scalaz._
import Scalaz._

import org.ini4j.Ini
import java.io.FileReader

object DBManager {
  val log = Logger(LoggerFactory.getLogger(this.getClass))

  val savedAvs = Map[String, List[String]]()

  private val connection = getConnection

  def getAvList(sql: String, columnName: String): List[String] = {
    val avList = List[String]()

    val stmt = this.connection.createStatement
    val rs = stmt.executeQuery(sql)

    while (rs.next) {
      avList ++= List(rs.getString(columnName))
    }

    stmt.close
    avList
  }

  private def getConnection(): Connection = {
    Class.forName(AppConfig.conf.getString("db.default.driver"))
    DriverManager.getConnection(
      AppConfig.conf.getString("db.default.url"),
      AppConfig.conf.getString("db.default.user"),
      AppConfig.conf.getString("db.default.password")
    )
  }
}
