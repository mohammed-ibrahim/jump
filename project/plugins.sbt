
addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.11.2")

addSbtPlugin("org.scala-sbt.plugins" % "sbt-onejar" % "0.8")

resolvers += "Sonatype OSS Releases" at "https://oss.sonatype.org/service/local/staging/deploy/maven2"

addSbtPlugin("org.scalariform" % "sbt-scalariform" % "1.4.0")

resolvers += "spray repo" at "http://repo.spray.io" // not needed for sbt >= 0.12

addSbtPlugin("io.spray" % "sbt-revolver" % "0.7.1")
