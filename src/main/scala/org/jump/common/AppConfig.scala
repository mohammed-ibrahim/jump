package org.jump.common

object AppConfig {
  var db = ""

  var dbUser = ""

  var dbUserPass = ""

  var logSql = false

  var verbose = false

  var filename = ""

  var host = "localhost"

  val driver = "com.mysql.jdbc.Driver"

  private val url = "jdbc:mysql://localhost/%s"

  val batchSize = 1000

  def getJdbcUrl(): String = {
    url.format(db)
  }
}
