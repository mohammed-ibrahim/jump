package org.jump

import java.io.FileReader
import scala.collection.JavaConversions._
import java.util.Date

import java.io.StringWriter
import java.io.PrintWriter
import java.io.File

import org.jump.factory._
import org.jump.manager._
import org.jump.db._
import org.jump.common._
import org.jump.logging._

import java.nio.file.Paths
import java.nio.file.Path
import java.nio.file.Files

import org.jump.parser.JumpGen
import org.jump.parser.JumpGen
import org.jump.parser.SqlCommand
import org.jump.parser.InsertCommand
import org.jump.parser.RollbackCommand
import org.jump.parser.ParseResult

object Boot {
  val log = LogManager.createInstance(this.getClass.getName)
  def main(args: Array[String]): Unit = {

    try {
      var fileName = ""
      if (args.length != 1) {
        println("Usage: java -jar jump-1.0.jar <input-configuration-file>")
        return
      }
      fileName = args(0)

      val file = new File(fileName)
      if (!file.exists()) {
        throw new RuntimeException(s"The configuration file [${fileName}] doesn't exists or is not accesssible to the application.")
      }

      val fileContents = getFileContents(fileName)
      val result = JumpGen.parse(fileContents)
      if (result.getCommands() == null) {
        log.info("There is an error in script: " + fileName);
        log.info(result.getErrorMessage());

        if (AppConfig.conf.getString("debug") == "true") {
          log.info(result.getStackTrace());
        }

        return ;
      }

      DBManager.init
      var index = 1
      result.getCommands().foreach { x =>
        runCommand(index.toString, x)
        index = index + 1
      }

      /*
      IniManager.ini.keySet.map { x =>
        runSection(x)
      }
      DBManager.commitAndClose
      log.info("Successfully completed: [COMMITED CHANGES]")
      */
    } catch {
      case e: Exception => {
        DBManager.rollbackAndClose
        val sw = new StringWriter
        e.printStackTrace(new PrintWriter(sw))
        log.severe(sw.toString)
        log.severe("FAILED! [ROLLED BACK CHANGES]")
      }
    }
  }

  def getFileContents(fileName: String): String = {
    val encoded = Files.readAllBytes(Paths.get(fileName));
    return new String(encoded);
  }

  def runCommand(commandNum: String, cmd: Any): Unit = {
    if (cmd.isInstanceOf[SqlCommand]) {
      val command = cmd.asInstanceOf[SqlCommand]
      val sqls = command.getSqls()

      sqls.foreach { x =>
        DBManager.execute(x)
      }
    } else if (cmd.isInstanceOf[InsertCommand]) {
      val command = cmd.asInstanceOf[InsertCommand]
      val fields = FieldFactory.build(command)
      ImportManager.process(commandNum, command, fields)
    } else if (cmd.isInstanceOf[RollbackCommand]) {
      throw new RuntimeException("Manual rollback called!! rolling back..")
    }
  }

  def runSection(sectionTag: String): Unit = {
    /*
    val runType = IniManager.getKey(sectionTag, "type")
    val startAt = (new Date).getTime
    val logSql = if (IniManager.getKey("db", "log_sql") == "true") true else false

    if (runType == "insert") {
      var fields = FieldFactory.build(sectionTag)
      var sqlList = ImportManager.process(sectionTag, fields, logSql)
    }

    if (runType == "sql") {
      val sql = IniManager.getKey(sectionTag, "sql")
      if (logSql) {
        log.info("Executing the sql: " + sql);
      }
      DBManager.execute(sql)
    }
    val endAt = (new Date).getTime

    log.info(s"[${sectionTag}] total time: ${(endAt - startAt).toString} ms")
    System.gc
    */
  }
}
