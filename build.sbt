import Dependencies._

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "com.example",
      scalaVersion := "2.12.3",
      version      := "0.1.0-SNAPSHOT"
    )),
    name := "Hello",
    PB.protoSources in Compile ++= Seq((sourceDirectory in Test).value / "protobuf"),
    /*PB.targets in Compile := Seq(
      scalapb.gen() -> (sourceManaged in Compile).value
    ),*/
    libraryDependencies ++= {
      Seq(
        scalaTest % Test,
        monix,
        monixKafka,
        scalapb,
        scalapbGrpc
      )
    }
  )
