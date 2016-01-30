package com.example

import com.typesafe.scalalogging._
import org.slf4j.LoggerFactory

import org.ini4j.Ini
import java.io.FileReader
import com.github.mirreck._

object FakeManager {
  val log = Logger(LoggerFactory.getLogger(this.getClass))

  val factory = new FakeFactory

  def getFake(input: String): String = {
    val method = factory.getClass.getMethod(input)
    method.invoke(factory).toString
  }
}
