package com.example

import com.typesafe.scalalogging._
import org.slf4j.LoggerFactory
import java.sql._

import org.ini4j._
import org.ini4j.Ini.Section
import java.io.FileReader

object ConfigManager {
  val log = Logger(LoggerFactory.getLogger(this.getClass))
  private var ini: Ini = null

  def load(fileName: String): Unit = {
    this.ini = new Ini(new FileReader(fileName))
  }

  def getStartSection(): Section = {
    ini.get("start")
  }

  def getSection(tag: String): Section = {
    ini.get(tag)
  }
}
