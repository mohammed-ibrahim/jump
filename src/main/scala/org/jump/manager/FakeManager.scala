package org.jump.manager

import java.io.FileReader
import com.github.mirreck._
import java.util.Random
import java.util.Calendar
import java.text.SimpleDateFormat
import scala.collection.JavaConversions._

object FakeManager {
  val factory = new FakeFactory

  val randomizer = new Random

  val months = List("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December")

  val genders = List("M", "F")

  val trueFalse = List("true", "false")

  val boolValues = List("0", "1")

  def getFake(input: String): String = {
    val method = factory.getClass.getMethod(input)
    method.invoke(factory).toString
  }

  def custom(input: String): String = {

    val supported = List("name", "name_title", "int", "long", "float", "double", "first_name", "last_name", "month", "year", "date",
      "current_date", "time_stamp", "zipcode", "country", "city", "address", "street_address", "latitude", "longitude", "color", "height",
      "phone_number", "email", "gender", "true_false", "boolean", "slug", "website", "url", "ten_dig_phone_num")

    input match {
      case "name"              => factory.name

      case "name_title"        => factory.nameTitle

      case "int"               => factory.digits(random(2, 8))

      case "long"              => factory.digits(random(4, 11))

      case "float"             => factory.digits(random(1, 3)) + "." + factory.digits(random(1, 2))

      case "double"            => factory.digits(random(3, 5)) + "." + factory.digits(random(1, 2))

      case "first_name"        => factory.firstName

      case "last_name"         => factory.lastName

      case "month"             => months(randomizer.nextInt(months.length))

      case "sentence"          => factory.sentence

      case "paragraph"         => factory.paragraph

      case "year"              => random(1990, 2050).toString

      case "date"              => getDate

      case "current_date"      => getCurrentDate

      case "time_stamp"        => getDate

      case "zipcode"           => factory.zipCode

      case "country"           => factory.country

      case "city"              => factory.city

      case "address"           => factory.fullAddress.toList.mkString(" ")

      case "street_address"    => factory.streetAddress

      case "latitude"          => factory.coordinatesLatLng()(0).toString

      case "longitude"         => factory.coordinatesLatLng()(1).toString

      case "color"             => factory.eyeColor

      case "height"            => factory.height.toString

      case "phone_number"      => factory.phoneNumber

      case "email"             => factory.email

      case "gender"            => genders(randomizer.nextInt(genders.length))

      case "true_false"        => trueFalse(randomizer.nextInt(trueFalse.length))

      case "boolean"           => boolValues(randomizer.nextInt(boolValues.length))

      case "slug"              => factory.words(random(2, 5)).mkString("").map(x => x.toLower)

      case "website"           => getUrl

      case "url"               => getUrl

      case "ten_dig_phone_num" => getTenDigNum

      case _ => {
        throw new RuntimeException(s"Not supported fake-param [${input}], only supported values are: [${supported.mkString(",")}]")
      }
    }
  }

  def random(low: Int, high: Int): Int = {
    randomizer.nextInt(high - low) + low
  }

  def getTenDigNum(): String = {
    val input = 8943767676L + random(1, 1000000)
    input.toString
  }

  def getCurrentDate(): String = {
    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(new java.util.Date)
  }

  def getDate(): String = {
    var cal = Calendar.getInstance

    var factor = -1
    if ((random(0, 9) % 2) == 0) {
      factor = 1
    }

    cal.add(Calendar.DATE, factor * random(10, 20000))
    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(cal.getTime)
  }

  def getUrl(): String = {
    "www." + factory.letters(random(10, 25)).map(x => x.toLower) + ".com"
  }
}
