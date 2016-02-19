package org.jump.common

import com.typesafe.scalalogging._
import org.slf4j.LoggerFactory
import scala.collection.JavaConversions._

import org.jump.parser._
import org.ini4j.Ini
import java.io.FileReader
import org.jump.manager._

object ParameterParser {
  val log = Logger(LoggerFactory.getLogger(this.getClass))

  def getFields(sectionTag: String): List[FieldConfig] = {
    val passedString = IniManager.getKey(sectionTag, "fields")
    var parsedFields = JumpGen.parseFields(passedString)

    if (parsedFields == null) {
      log.error(s"Passed string is [${passedString}]")
      throw new RuntimeException(s"The fields are not mentioned properly: [${passedString}] Refer the project documentation for field configuration.");
    }

    parsedFields.toList
  }
}
