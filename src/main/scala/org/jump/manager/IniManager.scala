package org.jump.manager

import java.sql._

import org.ini4j._
import org.ini4j.Ini._
import java.io.FileReader
import scala.collection.mutable.Map
import scala.collection.JavaConversions._

import org.jump.common._

object IniManager {
  var ini: Ini = null

  def load(fileName: String): Unit = {
    this.ini = new Ini(new FileReader(fileName))

    ini.keySet.map { x =>
      assertSectionExistsWithKeys(x, List("type"))
    }

    assertSectionExistsWithKeys("db", List("user", "password", "driver", "url"))
    ini.keySet.map{ x =>
      ini.get(x).get("type") match {
        case "insert" => {
          assertSectionExistsWithKeys(x, List("type", "table", "fields", "rows"))
        }

        case "db" => {
          //do nothing already verified above
        }

        case "sql" => {
          assertSectionExistsWithKeys(x, List("type", "sql"))
        }

        case _ => {
          throw new RuntimeException(s"Unknown configuration type [${ini.get(x).get("type")}] section [${x}]")
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
