import Dependencies._

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "mauricio",
      scalaVersion := "2.12.3",
      version      := "0.1.0-SNAPSHOT"
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
        cats
      )
    }
  )
