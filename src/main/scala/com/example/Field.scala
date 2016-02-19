package com.example

import com.typesafe.scalalogging._
import org.slf4j.LoggerFactory
import java.sql._
import com.typesafe.config.ConfigFactory

import org.ini4j.Ini
import java.io.FileReader
import org.jump.parser._

import java.util.Random
import scala.collection.JavaConversions._

import com.example.Formatter._

trait Field {
  def produce(): String

  def getName(): String
}

class CrawlerField(config: FieldConfig, crawler: Crawler) extends Field {
  def getName(): String = config.getFieldName

  def produce(): String = {
    wrap(escape(crawler.getNext))
  }
}

class FakerField(config: FieldConfig) extends Field {
  def getName(): String = config.getFieldName

  def produce(): String = {
    wrap(escape(FakeManager.custom(config.getParams.toList(0))))
  }
}
