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

  private val avIterator = Map[String, Iterator[String]]()

  def getRandom(avTag: String): String = {
    if (!savedAvs.keySet.contains(avTag)) {
      val sql = ConfigManager.getKey(avTag, "sql")

      val loadedItems = DBManager.getAvList(sql, "av")
      if (loadedItems.length < 1) {
        throw new RuntimeException(s"The section ${avTag} doesn't have any data in db")
      }
      savedAvs(avTag) = loadedItems
    }

    var requiredList = savedAvs(avTag)
    val randomizer = new Random
    requiredList(randomizer.nextInt(requiredList.length))
  }

  def getNext(avTag: String): String = {
    if (!savedAvs.keySet.contains(avTag)) {
      val sql = ConfigManager.getKey(avTag, "sql")

      val loadedItems = DBManager.getAvList(sql, "av")
      if (loadedItems.length < 1) {
        throw new RuntimeException(s"The section ${avTag} doesn't have any data in db")
      }
      savedAvs(avTag) = loadedItems
      avIterator(avTag) = loadedItems.iterator
    }

    if (avIterator(avTag).hasNext) {
      avIterator(avTag).next
    } else {
      avIterator(avTag) = savedAvs(avTag).iterator
      avIterator(avTag).next
    }
  }
}
