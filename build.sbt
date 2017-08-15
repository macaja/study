import Dependencies._
import sbt.addCommandAlias

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "mauricio",
      scalaVersion := "2.12.3"
    )),
    name := "Study",
    PB.protoSources in Compile ++= Seq((sourceDirectory in Test).value / "protobuf"),
    PB.targets in Compile := Seq(
      scalapb.gen() -> (sourceManaged in Compile).value
    ),
    libraryDependencies ++= {
      Seq(
        scalaTest % Test, // Library to use in tests
        monix, // Come with Task, Observable, ...
        monixKafka, // Allow use KafkProducer and Consumer to act with Kafka Topics
        scalaPb, // Generate case class's based on .proto's
        scalapbGrpc, // Allow generate services GRPC in protobuf
        pureconfig, // To load configuration
        ficus, //allow config.as[Config] in DefaultConfig
        cats,
        "org.apache.logging.log4j" % "log4j-1.2-api"             % "2.8.2",
        "org.apache.logging.log4j" % "log4j-api"                 % "2.8.2",
        "org.apache.logging.log4j" % "log4j-core"                % "2.8.2",
        "org.apache.logging.log4j" % "log4j-jcl"                 % "2.8.2",
        "org.apache.logging.log4j" % "log4j-jul"                 % "2.8.2",
        "org.apache.logging.log4j" % "log4j-slf4j-impl"          % "2.8.2"
      )
    },
    excludeDependencies ++= Seq(
      "log4j"                    % "log4j",
      "org.apache.logging.log4j" % "log4j-to-slf4j",
      "org.slf4j"                % "log4j-over-slf4j",
      "ch.qos.logback"           % "logback-core",
      "org.slf4j"                % "jcl-over-slf4j",
      "org.slf4j"                % "jul-to-slf4j"
    )
  )

addCommandAlias("validate", ";clean;test:scalafmt;test;it:test")
addCommandAlias("review", ";clean;coverage;test:scalafmt;test;it:test;coverageReport")
addCommandAlias("dependencies", ";dependencyUpdates;evicted")
