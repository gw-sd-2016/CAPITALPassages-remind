
name := "reMind"

version := "1.0"


libraryDependencies ++= Seq(
  // Select Play modules
  jdbc,      // The JDBC connection pool and the play.api.db API
  javaCore,  // The core Java API

  // WebJars pull in client-side web libraries
  "org.webjars" %% "webjars-play" % "2.2.0",
  "org.webjars" % "bootstrap" % "2.3.1",

  "mysql" % "mysql-connector-java" % "5.1.28",
  "org.apache.directory.studio" % "org.apache.commons.io" % "2.4",
  "com.google.code.gson" % "gson" % "2.2",
  "edu.stanford.nlp" % "stanford-corenlp" % "3.4.1"
)

libraryDependencies += evolutions


lazy val myProject = (project in file("."))
  .enablePlugins(PlayJava, PlayEbean)
