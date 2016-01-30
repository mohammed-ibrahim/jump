package com.example

import com.typesafe.scalalogging._
import org.slf4j.LoggerFactory

import org.ini4j._
import java.io.FileReader
import scala.collection.mutable.Map
import java.util.Random

import scalaz._
import Scalaz._

object AVManager {
  val log = Logger(LoggerFactory.getLogger(this.getClass))

  private val savedAvs = Map[String, List[String]]()

  def getRandom(avTag: String): String = {
    if (!savedAvs.keySet.contains(avTag)) {
      val sql = ConfigManager.getSection(avTag).fetch("sql")
      val loadedItems = DBManager.getAvList(sql, "av")
      savedAvs(avTag) = loadedItems
    }

    var requiredList = savedAvs(avTag)

    val randomizer = new Random
    requiredList(randomizer.nextInt(requiredList.length))
  }
}
