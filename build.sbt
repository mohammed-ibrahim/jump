import com.github.retronym.SbtOneJar._

import scalariform.formatter.preferences._

oneJarSettings

scalariformSettings

// Code formatting options
ScalariformKeys.preferences := ScalariformKeys.preferences.value
  .setPreference(AlignParameters, true)
  .setPreference(AlignArguments, true)
  .setPreference(MultilineScaladocCommentsStartOnFirstLine, true)
  .setPreference(SpacesAroundMultiImports, true)
  .setPreference(AlignSingleLineCaseStatements, true)
  .setPreference(DoubleIndentClassDeclaration, true)
  .setPreference(PreserveDanglingCloseParenthesis, true)
  .setPreference(CompactStringConcatenation, false)
  .setPreference(DoubleIndentClassDeclaration, false)
  .setPreference(PreserveDanglingCloseParenthesis, true)
  .setPreference(PreserveSpaceBeforeArguments, true)
  .setPreference(SpaceBeforeColon, false)
  .setPreference(SpaceInsideBrackets, false)
  .setPreference(SpaceInsideParentheses, false)
  .setPreference(IndentLocalDefs, false)
  .setPreference(IndentSpaces, 2)

mappings in oneJar += (file("src/main/resources/application.conf"),"models")
//mappings in oneJar += (file("src/main/resources/logback.xml"),"logback.xml")

name := """jump"""

version := "1.0"

scalaVersion := "2.11.7"

scalacOptions := Seq("-feature", "-unchecked", "-deprecation", "-encoding", "utf8")

// Change this to another test framework if you prefer
//libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.4" % "test"
libraryDependencies += "com.github.mirreck" % "java-fake-factory" % "1.0.2"
libraryDependencies += "mysql" % "mysql-connector-java" % "5.1.6"
libraryDependencies += "org.scalaz" % "scalaz-core_2.11" % "7.0.6"

// Uncomment to use Akka
//libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.3.11"
//libraryDependencies += "com.typesafe" % "config" % "1.2.1"

//Cli parser
libraryDependencies += "commons-cli" % "commons-cli" % "1.2"

//libraryDependencies ++= Seq(
//  "ch.qos.logback"  %  "logback-classic"   % "1.1.3",
//  "com.typesafe.scala-logging" %% "scala-logging" % "3.1.0"
//)

javaOptions += "-Xmx3G"

javaOptions += "-XX:+UseG1GC"

javaOptions += "-XX:MaxGCPauseMillis=100"

javaOptions += "-XX:MinHeapFreeRatio=30"

javaOptions += "-XX:MaxHeapFreeRatio=50"


fork in run := false

Revolver.settings
