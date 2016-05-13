package org.jump.db

import java.sql._

import scalaz._
import Scalaz._

import java.io.FileReader
import scala.collection.JavaConversions._

import org.jump.manager._
import org.jump.common._

object DBManager {

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
    Class.forName(AppConfig.conf.getString("db.default.driver"))
    val conn = DriverManager.getConnection(
      AppConfig.conf.getString("db.default.url"),
      AppConfig.conf.getString("db.default.user"),
      AppConfig.conf.getString("db.default.password")
    )

    conn.setAutoCommit(false)
    conn
  }

  def execute(sql: String): Unit = {
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
