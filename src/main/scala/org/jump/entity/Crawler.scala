package org.jump.entity

import java.sql._
import com.typesafe.config.ConfigFactory

import java.io.FileReader

import java.util.Random

class Crawler {
  private var contentList: List[String] = null
  private var mode: String = null
  private var iterationPerItem: Int = 0
  private var index: Int = 0;
  private var hitsOnIndex: Int = 0

  def this(contentList: List[String], mode: String, iterationPerItem: Int, tag: String) {
    this()
    if (contentList.size < 1) {
      throw new RuntimeException(s"The list item doens't have any item [${tag}]")
    }

    this.contentList = contentList

    if (iterationPerItem < 1) {
      throw new RuntimeException(s"Invalid item given for iterator [${iterationPerItem}]")
    }

    this.iterationPerItem = iterationPerItem

    mode match {
      case "random" => this.mode = "random"

      case "serial" => this.mode = "serial"

      case _ => {
        throw new RuntimeException(s"Invalid iteration mode [${mode}]")
      }
    }
  }

  def getNext(): String = {
    this.mode match {
      case "random" => this.fetchRandom

      case "serial" => this.fetchSerial

      case _ => {
        throw new RuntimeException(s"Invalid iteration mode [${mode}]")
      }
    }
  }

  private def fetchRandom(): String = {
    var randomizer = new Random()
    contentList(randomizer.nextInt(contentList.size))
  }

  private def fetchSerial(): String = {
    val item = contentList(index)

    hitsOnIndex = hitsOnIndex + 1
    if (hitsOnIndex >= iterationPerItem) {
      hitsOnIndex = 0
      index = index + 1

      if (index >= contentList.size) {
        index = 0
      }
    }

    item
  }
}
