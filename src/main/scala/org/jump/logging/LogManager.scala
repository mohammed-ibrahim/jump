package org.jump.logging

import org.ini4j.Ini
import java.io.FileReader
import com.github.mirreck._
import java.util.Random
import java.util.Calendar
import java.text.SimpleDateFormat
import scala.collection.JavaConversions._
import java.util.logging._

object LogManager {

  def createInstance(name: String): Logger = {
    var logger = Logger.getLogger(name)
    logger.getParent.getHandlers.map { x =>
      logger.getParent.removeHandler(x)
    }

    val handlers = logger.getHandlers

    handlers.map { x => logger.removeHandler(x) }
    val consoleHandler = new ConsoleHandler

    consoleHandler.setFormatter(new BriefFormatter)
    logger.addHandler(consoleHandler)

    logger
  }
}

class BriefFormatter extends Formatter {
  def format(record: LogRecord): String = { record.getMessage + System.getProperty("line.separator") }
}
