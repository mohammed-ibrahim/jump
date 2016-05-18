package org.jump.db

import java.sql._

import scalaz._
import Scalaz._

import java.io.FileReader
import scala.collection.JavaConversions._

import org.jump.manager._
import org.jump.common._
import org.jump.logging._

object DBManager {

  val log = LogManager.createInstance(this.getClass.getName)
  private var connection: Connection = null

  def init() = {
    connection = getConnection
  }

  def getAvList(sql: String): List[String] = {
    if (AppConfig.logSql) {
      log.info(sql)
    }

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
    Class.forName(AppConfig.driver)
    val conn = DriverManager.getConnection(
      AppConfig.getJdbcUrl,
      AppConfig.dbUser,
      AppConfig.dbUserPass
    )

    conn.setAutoCommit(false)
    conn
  }

  def execute(sql: String): Unit = {
    if (AppConfig.logSql) {
      log.info(sql)
    }

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
