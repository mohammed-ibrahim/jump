package org.jump

import org.jump.common._
import org.jump.logging._

import org.apache.commons.cli.GnuParser
import org.apache.commons.cli.CommandLine
import org.apache.commons.cli.CommandLineParser
import org.apache.commons.cli.HelpFormatter
import org.apache.commons.cli.Option
import org.apache.commons.cli.Options
import org.apache.commons.cli.ParseException

import org.apache.commons.cli.UnrecognizedOptionException
import org.apache.commons.cli.HelpFormatter

object CloValidator {
  val log = LogManager.createInstance(this.getClass.getName)

  def isValid(args: Array[String]): Boolean = {
    try {
      val options = new Options
      options.addOption(new Option("d", "database", true, "Name of the database where to which the changes are to be made."))
      options.addOption(new Option("u", "username", true, "Database username."))
      options.addOption(new Option("p", "password", true, "Password for the usename."))
      options.addOption(new Option("f", "file", true, "Input jump script file path."))
      options.addOption(new Option("l", "logsql", false, "Log the sql statements that are executed."))
      options.addOption(new Option("v", "verbose", false, "Verbose."))
      options.addOption(new Option("h", "help", false, "Help page."))
      options.addOption(new Option("r", "dry-run", false, "Dry run, rollback the change after the completion of import, Note: Rollback only works for import changes and does not rollback schema changes."))

      val parser = new GnuParser

      val commandLine = parser.parse(options, args)

      if (commandLine.hasOption("help")) {
        displayHelp(options)
        return false
      }

      if (!commandLine.hasOption("file")) {
        log.info("-f or --file option is required")
        return false
      }
      AppConfig.filename = commandLine.getOptionValue("file")

      if (!commandLine.hasOption("database")) {
        log.info("-db or --database option is required")
        return false
      }
      AppConfig.db = commandLine.getOptionValue("database")

      if (!commandLine.hasOption("username")) {
        log.info("-u or --username option is required")
        return false
      }
      AppConfig.dbUser = commandLine.getOptionValue("username")

      if (commandLine.hasOption("password")) {
        AppConfig.dbUserPass = commandLine.getOptionValue("password")
      }

      if (commandLine.hasOption("logsql")) {
        AppConfig.logSql = true
      }

      if (commandLine.hasOption("verbose")) {
        AppConfig.verbose = true
      }

      if (commandLine.hasOption("dry-run")) {
        AppConfig.dryrun = true
      }

    } catch {
      case ue: UnrecognizedOptionException => {
        log.info(ue.getMessage)
        return false
      }

      case e: Exception => {
        log.info(e.getMessage)
        return false
      }
    }

    return true
  }

  def displayHelp(options: Options) {
    val formater = new HelpFormatter

    formater.printHelp("java -jar jump.jar --file <file_path> --database <database_name> --username <user_name> --password <password>", options)
  }
}
