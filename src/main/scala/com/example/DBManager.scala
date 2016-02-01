package com.example

import com.typesafe.scalalogging._
import org.slf4j.LoggerFactory
import java.sql._

import scalaz._
import Scalaz._

import org.ini4j.Ini
import java.io.FileReader
import scala.collection.JavaConversions._

object DBManager {
  val log = Logger(LoggerFactory.getLogger(this.getClass))

  val savedAvs = Map[String, List[String]]()

  private var connection: Connection = null

  def init() = {
    connection = getConnection
  }

  def getAvList(sql: String, columnName: String): List[String] = {
    val avList = new java.util.ArrayList[String]()

    val stmt = this.connection.createStatement
    val rs = stmt.executeQuery(sql)

    while (rs.next) {
      avList.add(rs.getString(columnName))
    }

    stmt.close
    avList.toList
  }

  private def getConnection(): Connection = {
    Class.forName(ConfigManager.getKey("db", "driver"))
    val conn = DriverManager.getConnection(
      ConfigManager.getKey("db", "url"),
      ConfigManager.getKey("db", "user"),
      ConfigManager.getKey("db", "password")
    )

    conn.setAutoCommit(false)
    conn
  }

  def executeInsert(sql: String): Unit = {
    val stmt = connection.createStatement
    stmt.executeUpdate(sql)
    stmt.close
  }

  def closeConn(rollback: Boolean) = {
    if (connection != null) {
      if (rollback) {
        connection.rollback
      } else {
        connection.commit
      }

      connection.close
    }
  }
}
