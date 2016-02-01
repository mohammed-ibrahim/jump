package com.example

import com.typesafe.scalalogging._
import org.slf4j.LoggerFactory
import java.sql._

import org.ini4j._
import org.ini4j.Ini._
import java.io.FileReader
import scala.collection.mutable.Map
import scala.collection.JavaConversions._

object ConfigManager {
  val log = Logger(LoggerFactory.getLogger(this.getClass))
  var ini: Ini = null

  def load(fileName: String): Unit = {
    this.ini = new Ini(new FileReader(fileName))

    ini.keySet.map { x =>
      assertSectionExistsWithKeys(x, List("type"))
    }

    assertSectionExistsWithKeys("db", List("user", "password", "driver", "url"))
    ini.keySet.map{ x =>
      ini.get(x).get("type") match {
        case "import" => {
          assertSectionExistsWithKeys(x, List("type", "table", "fields", "stragegy", "rows"))
        }

        case "permissible-values" => {
          assertSectionExistsWithKeys(x, List("type", "sql"))
        }

        case "db" => {
          //do nothing already verified above
        }

        case _ => {
          throw new RuntimeException(s"Unknown configuration type ${x}")
        }
      }
    }
  }

  def assertSectionExists(sectionTag: String) = {
    var section = ini.get(sectionTag)

    if (section == null) {
      throw new RuntimeException(s"Config section not found [${sectionTag}], This section is mandatory")
    }
  }

  def assertSectionExistsWithKeys(sectionTag: String, keys: List[String]) = {
    var section = ini.get(sectionTag)
    if (section == null) {
      throw new RuntimeException(s"Config section not found [${sectionTag}]")
    }

    var missingKeySet = (keys.toSet -- section.keySet)
    if (missingKeySet.size > 0) {
      throw new RuntimeException(s"Key(s) [${missingKeySet.toList.mkString(",")}] are missing in section [${sectionTag}]")
    }
  }

  def getKey(sectionTag: String, key: String): String = {
    var section = ini.get(sectionTag)
    if (section == null) {
      throw new RuntimeException(s"Config section not found[${sectionTag}]")
    }

    var result = ini.get(sectionTag).fetch(key)
    if (result == null) {
      throw new RuntimeException(s"Config key [${key}] not found in section [${sectionTag}]")
    }

    result
  }
}
