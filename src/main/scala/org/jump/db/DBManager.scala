package org.jump.db

import com.typesafe.scalalogging._
import org.slf4j.LoggerFactory
import java.sql._

import scalaz._
import Scalaz._

import org.ini4j.Ini
import java.io.FileReader
import scala.collection.JavaConversions._

import org.jump.manager._

object DBManager {
  val log = Logger(LoggerFactory.getLogger(this.getClass))

  private var connection: Connection = null

  def init() = {
    connection = getConnection
  }

  def getAvList(sql: String): List[String] = {
    val avList = new java.util.ArrayList[String]()

    val stmt = this.connection.createStatement
    val rs = stmt.executeQuery(sql)

    while (rs.next) {
      avList.add(rs.getString(1))
    }

    stmt.close
    avList.toList
  }

  private def getConnection(): Connection = {
    Class.forName(IniManager.getKey("db", "driver"))
    val conn = DriverManager.getConnection(
      IniManager.getKey("db", "url"),
      IniManager.getKey("db", "user"),
      IniManager.getKey("db", "password")
    )

    conn.setAutoCommit(false)
    conn
  }

  def executeInsert(sql: String): Unit = {
    val stmt = connection.createStatement
    stmt.executeUpdate(sql)
    stmt.close
  }

  def commitAndClose() = {
    if (connection != null) {
      connection.commit
      connection.close
    }
  }

  def rollbackAndClose() = {
    if (connection != null) {
      connection.rollback
      connection.close
    }
  }
}
