package org.jump.common

import java.io.FileReader
import scala.collection.JavaConversions._

object Formatter {
  def wrap(term: String): String = {
    "'" + term + "'"
  }

  def escape(term: String): String = {
    term.replace("'", "\\'")
  }
}
